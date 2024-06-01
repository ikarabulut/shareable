package com.ikarabulut.shareable.account.common.exceptions;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException(String msg) {
        super(msg);
    }
}
