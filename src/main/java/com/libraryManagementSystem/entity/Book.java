package com.libraryManagementSystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    private String bookId;

    @NonNull
    private String bookName;

    @NonNull
    private String authorName;

    private int semester;

    @NonNull
    private String branch;

    private int totalCopies;

    private int availableCopies;

}
