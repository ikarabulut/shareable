package com.ikarabulut.shareable.handlers.request;

import com.ikarabulut.shareable.common.AllowedFileTypes;
import com.ikarabulut.shareable.exceptions.FileExtensionNotAllowed;

public class FileRequestHandler {

    public FileRequestHandler(){}

    public String validateFileType(String fileName){
        var extensionIndex = fileName.lastIndexOf('.');
        var extension = fileName.substring(extensionIndex);

        if (!AllowedFileTypes.isFileTypeAllowed(extension)) {
            throw new FileExtensionNotAllowed(extension + " is not an allowed extension");
        }
        return extension;
    }
}
