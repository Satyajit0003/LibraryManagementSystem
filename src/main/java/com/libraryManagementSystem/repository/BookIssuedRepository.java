package com.libraryManagementSystem.repository;

import com.libraryManagementSystem.entity.BookIssued;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookIssuedRepository extends MongoRepository<BookIssued, ObjectId> {
    void deleteById(@NonNull ObjectId id);
}
