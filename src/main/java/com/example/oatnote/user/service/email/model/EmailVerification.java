package com.example.oatnote.user.service.email.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;

@Getter
@Document(collection = "email_verifications")
public class EmailVerification {

    @Id
    private String email;

    private String code;

    @Field("eTime")
    private LocalDateTime expiryTime;

    public EmailVerification(String email, String code) {
        this.email = email;
        this.code = code;
        this.expiryTime = LocalDateTime.now().plusMinutes(10);
    }
}
