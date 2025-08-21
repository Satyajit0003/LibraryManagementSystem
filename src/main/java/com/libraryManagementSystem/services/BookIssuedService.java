package com.libraryManagementSystem.services;

import com.libraryManagementSystem.entity.BookIssued;

import java.util.List;

public interface BookIssuedService {
    BookIssued issuedBook(String id);

    BookIssued returnedBook(String id);

    BookIssued findById(String id);

    List<BookIssued> getAllIssuedBook();

    void deleteById(String id, String name);
}
