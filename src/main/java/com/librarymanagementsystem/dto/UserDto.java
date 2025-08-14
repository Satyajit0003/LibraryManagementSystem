package com.librarymanagementsystem.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NonNull
    private String userName;

    private String email;

    @NonNull
    private String password;

}
