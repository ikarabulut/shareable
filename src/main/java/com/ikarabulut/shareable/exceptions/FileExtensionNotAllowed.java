package com.ikarabulut.shareable.exceptions;

public class FileExtensionNotAllowed extends RuntimeException {
    public FileExtensionNotAllowed(String msg) {
        super(msg);
    }
}
