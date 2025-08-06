package com.libraryManagementSystem.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private ObjectId userId;

    @NonNull
    @Indexed(unique = true)
    private String userName;

    @NonNull
    private String password;

    @NonNull
    private String email;

    private List<String> roles = new ArrayList<>();

    private LocalDateTime currentDate = LocalDateTime.now();

    @DBRef
    private List<BookIssued> bookIssuedList = new ArrayList<>();
}
