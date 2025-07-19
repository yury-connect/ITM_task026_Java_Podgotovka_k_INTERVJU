package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class BookException extends RuntimeException {

    public BookException(String message) {
        super(message);
    }

    public BookException(String message, HttpStatus status) {
        super(message);
    }
}
