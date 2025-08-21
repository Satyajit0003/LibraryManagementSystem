package com.libraryManagementSystem.repository;

import com.libraryManagementSystem.entity.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String> {
    Optional<Book> findByBookName(String bookName);

    List<Book> findBySemesterAndBranch(int semester, String branch);
}
