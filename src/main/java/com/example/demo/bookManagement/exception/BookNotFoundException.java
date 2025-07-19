package com.example.demo.bookManagement.exception;

import org.springframework.http.HttpStatus;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(String message) {
        super(message);
    }

    public BookNotFoundException(String message, HttpStatus status) {
        super(message);
    }
}
