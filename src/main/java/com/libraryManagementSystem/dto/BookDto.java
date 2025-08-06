package com.libraryManagementSystem.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

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
