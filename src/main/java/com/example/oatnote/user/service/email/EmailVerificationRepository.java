package com.example.oatnote.user.service.email;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.oatnote.user.service.email.model.EmailVerification;

public interface EmailVerificationRepository extends MongoRepository<EmailVerification, String> {

    Optional<EmailVerification> findByEmail(String email);

    void deleteByEmail(String email);

    boolean existsByEmail(String email);
}
