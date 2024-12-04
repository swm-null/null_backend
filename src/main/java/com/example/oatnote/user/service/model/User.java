package com.example.oatnote.user.service.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String email;

    @Field(name = "pwd")
    private String password;

    private String name;

    @Field(name = "profileImgUrl")
    private String profileImageUrl;

    @Field(name = "cTime")
    private LocalDateTime createdAt;

    @Field(name = "uTime")
    private LocalDateTime updatedAt;

    public User(String email, String password, String name, String profileImageUrl) {
        this.id = UUID.randomUUID().toString();
        this.email = email;
        this.password = password;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
        this.updatedAt = LocalDateTime.now();
    }

    public void update(String email, String name, String profileImageUrl) {
        this.email = email;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.updatedAt = LocalDateTime.now();
    }
}
