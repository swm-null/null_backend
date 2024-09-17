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

    @Field("expiry_date")
    private LocalDateTime expiryDate;

    public EmailVerification(String email, String code) {
        this.email = email;
        this.code = code;
        this.expiryDate = LocalDateTime.now().plusMinutes(10);
    }
}
