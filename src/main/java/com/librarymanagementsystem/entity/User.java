package com.librarymanagementsystem.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

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


    private String currentDate;

    @DBRef
    private List<BookIssued> bookIssuedList = new ArrayList<>();
}
