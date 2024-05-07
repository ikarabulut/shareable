package com.ikarabulut.shareable.web.controllers;

import com.ikarabulut.shareable.common.exceptions.ResourceNotFoundException;
import com.ikarabulut.shareable.web.handlers.request.FileRequestHandler;
import com.ikarabulut.shareable.common.models.FileModel;
import com.ikarabulut.shareable.web.repository.FileRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;


@RestController
public class FileController {

    @Autowired
    private FileRepository fileRepository;
    private final FileRequestHandler requestHandler = new FileRequestHandler();

    @PostMapping(path="/files")
    public ResponseEntity<FileModel> createFile(@RequestBody @Valid FileModel fileModel) {
        requestHandler.validateFileType(fileModel.getName());
        FileModel createdFile = this.fileRepository.save(fileModel);
        return new ResponseEntity<>(createdFile, HttpStatus.CREATED);
    }

    @PostMapping(path="/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("uuid") @Valid UUID uuid, @RequestParam("file") MultipartFile file) throws IOException {
        Optional<FileModel> fileObj = this.fileRepository.findByUuid(uuid);

        if (fileObj.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        FileModel fileRecord = fileObj.get();
        String extension = requestHandler.validateFileType(fileRecord.getName());

        var uploadPath = Path.of("upload-store/", file.getOriginalFilename());
        Files.createDirectories(uploadPath.getParent());

        fileRecord.setExtension(extension);
        try (var inputStream = file.getInputStream()) {
            Files.copy(inputStream, uploadPath,
                    StandardCopyOption.REPLACE_EXISTING
                    );
        }


        return new ResponseEntity<>("File uploaded", HttpStatus.CREATED);
    }

    @GetMapping(path="/files")
    public ResponseEntity<Iterable<FileModel>> listFiles() {
        Iterable<FileModel> files = this.fileRepository.findAll();
        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    @GetMapping(path="/files/{id}")
    public ResponseEntity<FileModel> listFile(@PathVariable("id") @Valid Long id) {
        FileModel file = this.fileRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No file found with Id: " + id));
        return new ResponseEntity<>(file, HttpStatus.OK);
    }

    @DeleteMapping(path="/files/{id}")
    public ResponseEntity<FileModel> deleteFile(@PathVariable("id") @Valid Long id) {
        Optional<FileModel> fileObj = this.fileRepository.findById(id);

        if (fileObj.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        FileModel fileRecord = fileObj.get();

        this.fileRepository.delete(fileRecord);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}