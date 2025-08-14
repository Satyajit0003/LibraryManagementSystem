package com.librarymanagementsystem.services;

import com.librarymanagementsystem.dto.BookDto;
import com.librarymanagementsystem.entity.Book;

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

