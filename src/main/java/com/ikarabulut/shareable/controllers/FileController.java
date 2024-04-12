package com.ikarabulut.shareable.controllers;

import com.ikarabulut.shareable.models.FileModel;
import com.ikarabulut.shareable.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class FileController {

    @Autowired
    private FileRepository fileRepository;

    @PostMapping(path="/files")
    public @ResponseBody String createFile(@RequestBody FileModel fileModel) {
        Integer createdId = this.fileRepository.save(fileModel).getId();
        return "Saved ID:: " + createdId;
    }

}
