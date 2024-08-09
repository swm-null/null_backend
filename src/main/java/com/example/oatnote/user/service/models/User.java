package com.example.oatnote.user.service.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Document(collection = "users")
@Getter
public class User {

    @Id
    private String id;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
