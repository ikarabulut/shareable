package com.ikarabulut.shareable.file_server.common.exceptions;

public class FileExtensionNotAllowed extends RuntimeException {
    public FileExtensionNotAllowed(String msg) {
        super(msg);
    }
}
