package com.example.oatnote.user.service.models;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.Instant;

@Document(collection = "users")
@Getter
public class User {

    @Id
    private String id;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @CreatedDate
    @Field(name = "ca")
    private Instant createdAt;

    @LastModifiedDate
    @Field(name = "ua")
    private Instant updatedAt;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
