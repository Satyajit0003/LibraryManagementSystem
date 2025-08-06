package com.libraryManagementSystem.dto;

import com.libraryManagementSystem.entity.Book;
import lombok.*;
import org.bson.types.ObjectId;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookIssuedDto {

    @NonNull
    private ObjectId bookId;

    private LocalDate issueDate;

    private LocalDate returnDate;

    private boolean isReturned;
}
