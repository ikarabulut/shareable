package com.ikarabulut.shareable.controllers;

import com.ikarabulut.shareable.exceptions.ResourceNotFoundException;
import com.ikarabulut.shareable.models.FileModel;
import com.ikarabulut.shareable.repository.FileRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
public class FileController {

    @Autowired
    private FileRepository fileRepository;

    @PostMapping(path="/files")
    public ResponseEntity<FileModel> createFile(@RequestBody @Valid FileModel fileModel) {
        FileModel createdFile = this.fileRepository.save(fileModel);
        return new ResponseEntity<>(createdFile, HttpStatus.CREATED);
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
