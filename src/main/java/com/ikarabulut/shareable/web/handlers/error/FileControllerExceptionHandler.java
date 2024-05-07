package com.ikarabulut.shareable.web.handlers.error;

import com.ikarabulut.shareable.common.exceptions.FileExtensionNotAllowed;
import com.ikarabulut.shareable.common.exceptions.ResourceNotFoundException;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;


@RestControllerAdvice
public class FileControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(FileExtensionNotAllowed.class)
    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
    public ErrorMessage fileExtensionNotAllowed(FileExtensionNotAllowed ex, WebRequest request) {
        return new ErrorMessage(
                HttpStatus.NOT_ACCEPTABLE.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage globalExceptionHandler(Exception ex, WebRequest request) {
        return new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
    }
}
