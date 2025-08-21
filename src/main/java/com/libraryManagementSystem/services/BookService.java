package com.libraryManagementSystem.services;

import com.libraryManagementSystem.dto.BookDto;
import com.libraryManagementSystem.entity.Book;

import java.util.List;

public interface BookService {

    Book createBook(BookDto bookDto);

    Book findByBookId(String id);

    Book findByBookName(String name);

    List<Book> getAllBook();

    Book updateBook(BookDto bookDto, String id);

    List<Book> findBySemesterAndBranch(int semester, String branch);

    void deleteById(String id);
}

