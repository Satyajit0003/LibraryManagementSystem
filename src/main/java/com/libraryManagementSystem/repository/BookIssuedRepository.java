package com.libraryManagementSystem.repository;

import com.libraryManagementSystem.entity.BookIssued;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookIssuedRepository extends MongoRepository<BookIssued, String> {
    void deleteById(String id);
}
