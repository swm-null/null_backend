package com.example.oatnote.user.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;

@Document(collection = "users")
@Getter
public class User {

    @Id
    private String id;

    private String email;

    private String password;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
