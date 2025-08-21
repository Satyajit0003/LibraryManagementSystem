package com.libraryManagementSystem.exception;

public class BookIssuedNotFoundException extends RuntimeException {
    public BookIssuedNotFoundException(String message) {
        super(message);
    }
}
