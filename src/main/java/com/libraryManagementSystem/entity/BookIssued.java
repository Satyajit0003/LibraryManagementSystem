package com.libraryManagementSystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

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
