package com.ikarabulut.shareable.file_server.web.controllers;

import com.ikarabulut.shareable.file_server.common.models.FileModel;
import com.ikarabulut.shareable.file_server.service.CloudStorageService;
import com.ikarabulut.shareable.file_server.web.handlers.request.FileRequestHandler;
import com.ikarabulut.shareable.file_server.web.repository.FileRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;


@RestController
@RequestMapping(value = "/api")
public class FileController {
    @Autowired
    private CloudStorageService cloudStorageService;
    @Autowired
    private FileRequestHandler fileRequestHandler;

    @PostMapping(path="/file")
    public ResponseEntity<FileModel> createFile(@RequestBody @Valid FileModel fileModel,
                                                @RequestHeader(name = "X-token", required = true) String token,
                                                HttpServletRequest request) {
        var createdFile = fileRequestHandler.createFileObject(fileModel);
        return new ResponseEntity<>(createdFile, HttpStatus.CREATED);
    }

    @PostMapping(path="/file/{uuid}/upload")
    public ResponseEntity<FileModel> uploadFile(@PathVariable("uuid") @Valid UUID uuid,
                                             @RequestParam("file") MultipartFile file,
                                             @RequestHeader(name = "X-token", required = true) String token) throws IOException {
        var response = this.fileRequestHandler.uploadFileToS3(file, uuid);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path="/file/all")
    public ResponseEntity<Iterable<FileModel>> listFiles() {
        return new ResponseEntity<>(fileRequestHandler.getAllFileObjects(), HttpStatus.OK);
    }

    @GetMapping(path="/file/{id}")
    public ResponseEntity<FileModel> listFile(@PathVariable("id") @Valid Long id) {
        return new ResponseEntity<>(fileRequestHandler.getFileObject(id), HttpStatus.OK);
    }

    @DeleteMapping(path="/file/{id}")
    public ResponseEntity<FileModel> deleteFile(@PathVariable("id") @Valid Long id) {
        fileRequestHandler.deleteFile(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
