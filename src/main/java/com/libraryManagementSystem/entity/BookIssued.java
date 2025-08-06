package com.libraryManagementSystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "book_issued")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookIssued {

    @Id
    private ObjectId issuedId;

    @NonNull
    private ObjectId bookId;

    @CreatedDate
    private LocalDate issueDate;

    private LocalDate returnDate;

    private boolean isReturned;

}
