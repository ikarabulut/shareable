package com.ikarabulut.shareable.controllers;

import com.ikarabulut.shareable.models.FileModel;
import com.ikarabulut.shareable.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class FileController {

    @Autowired
    private FileRepository fileRepository;

    @PostMapping(path="/files")
    public ResponseEntity<FileModel> createFile(@RequestBody FileModel fileModel) {
        FileModel createdFile = this.fileRepository.save(fileModel);
        return new ResponseEntity<>(createdFile, HttpStatus.CREATED);
    }

}
