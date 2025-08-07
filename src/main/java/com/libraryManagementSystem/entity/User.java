package com.libraryManagementSystem.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
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
    private String userId;

    @NonNull
    @Indexed(unique = true)
    private String userName;

    @NonNull
    private String password;

    @NonNull
    private String email;

    private List<String> roles = new ArrayList<>();

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime currentDate = LocalDateTime.now();

    @DBRef
    private List<BookIssued> bookIssuedList = new ArrayList<>();
}
