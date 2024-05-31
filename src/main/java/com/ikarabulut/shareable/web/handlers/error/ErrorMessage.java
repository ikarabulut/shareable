package com.ikarabulut.shareable.web.handlers.error;
import java.util.Date;

public record ErrorMessage(int statusCode, Date timestamp, String message, String description) {
}