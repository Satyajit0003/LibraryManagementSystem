package com.librarymanagementsystem.repository;

import com.librarymanagementsystem.entity.BookIssued;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookIssuedRepository extends MongoRepository<BookIssued, String> {
    void deleteById(String id);
}
