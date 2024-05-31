package com.ikarabulut.shareable.web.controllers;

import com.ikarabulut.shareable.common.AllowedFileTypes;
import com.ikarabulut.shareable.common.exceptions.FileExtensionNotAllowed;
import com.ikarabulut.shareable.common.exceptions.ResourceNotFoundException;
import com.ikarabulut.shareable.common.models.FileModel;
import com.ikarabulut.shareable.service.CloudStorageService;
import com.ikarabulut.shareable.web.repository.FileRepository;
import jakarta.validation.Valid;
import org.apache.tika.Tika;
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
    @Autowired
    private CloudStorageService cloudStorageService;

    @PostMapping(path="/file")
    public ResponseEntity<FileModel> createFile(@RequestBody @Valid FileModel fileModel) {
        var extensionIndex = fileModel.getName().lastIndexOf('.');
        var extension = fileModel.getName().substring(extensionIndex);
        if (!AllowedFileTypes.isFileTypeAllowed(extension)) {
            throw new FileExtensionNotAllowed(extension + " is not an allowed extension");
        }

        fileModel.setExtension(extension);
        FileModel createdFile = this.fileRepository.save(fileModel);

        return new ResponseEntity<>(createdFile, HttpStatus.CREATED);
    }

    @PostMapping(path="/file/{uuid}/upload")
    public ResponseEntity<String> uploadFile(@PathVariable("uuid") @Valid UUID uuid, @RequestParam("file") MultipartFile file) throws IOException {
        Optional<FileModel> fileObj = this.fileRepository.findByUuid(uuid);

        if (fileObj.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        FileModel fileRecord = fileObj.get();

        Tika tika = new Tika();
        if (!AllowedFileTypes.getByExtension(fileRecord.getExtension()).getMimeType().equals(tika.detect(file.getBytes()))) {
            return new ResponseEntity<>("Invalid content type. Expected " + AllowedFileTypes.getByExtension(fileRecord.getExtension()) + " but got " + tika.detect(file.getBytes()), HttpStatus.BAD_REQUEST);
        }

        var response = cloudStorageService.uploadFileToBucket(fileRecord.getName(), file);

        return new ResponseEntity<>("File uploaded:: " + response, HttpStatus.CREATED);
    }

    @GetMapping(path="/file/all")
    public ResponseEntity<Iterable<FileModel>> listFiles() {
        Iterable<FileModel> files = this.fileRepository.findAll();
        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    @GetMapping(path="/file/{id}")
    public ResponseEntity<FileModel> listFile(@PathVariable("id") @Valid Long id) {
        FileModel file = this.fileRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No file found with Id: " + id));
        return new ResponseEntity<>(file, HttpStatus.OK);
    }

    @DeleteMapping(path="/file/{id}")
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
