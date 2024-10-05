package com.example.oatnote.domain.user.service.email;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.oatnote.domain.user.service.email.model.EmailVerification;

public interface EmailVerificationRepository extends MongoRepository<EmailVerification, String> {

    Optional<EmailVerification> findByEmail(String email);
}
