package com.libraryManagementSystem.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDate;

@Document(collection = "book_issued")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookIssued implements Serializable {
    @Id
    private String issuedId;

    @NonNull
    private String bookId;

    private String issueDate;

    private String returnDate;

    private boolean isReturned;

}
