package com.librarymanagementsystem.exception;

public class BookIssuedNotFoundException extends RuntimeException {
    public BookIssuedNotFoundException(String message) {
        super(message);
    }
}
