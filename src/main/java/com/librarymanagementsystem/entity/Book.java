package com.librarymanagementsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;


@Document(collection = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book implements Serializable {

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
