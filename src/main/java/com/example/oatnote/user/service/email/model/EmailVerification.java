package com.example.oatnote.user.service.email.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Document(collection = "email_verifications")
public class EmailVerification {

    @Id
    private String id;

    private String email;

    private String code;

    @Field("eTime")
    private LocalDateTime expiryTime;

    public EmailVerification(String email, String code, LocalDateTime expiryTime) {
        this.id = UUID.randomUUID().toString();
        this.email = email;
        this.code = code;
        this.expiryTime = expiryTime;
    }
}
