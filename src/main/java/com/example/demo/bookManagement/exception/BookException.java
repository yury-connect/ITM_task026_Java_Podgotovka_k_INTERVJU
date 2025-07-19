package com.example.demo.bookManagement.exception;

import org.springframework.http.HttpStatus;

public class BookException extends RuntimeException {

    public BookException(String message) {
        super(message);
    }

    public BookException(String message, HttpStatus status) {
        super(message);
    }
}
