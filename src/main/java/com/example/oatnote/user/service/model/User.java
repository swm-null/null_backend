package com.example.oatnote.user.service.model;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;

@Document(collection = "users")
@Getter
public class User {

    @Id
    private UUID id;

    @Indexed(unique = true)
    private String email;

    @Field(name = "pwd")
    private String password;

    @Field(name = "cTime")
    @CreatedDate
    private Instant createdAt;

    @Field(name = "uTime")
    @LastModifiedDate
    private Instant updatedAt;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
