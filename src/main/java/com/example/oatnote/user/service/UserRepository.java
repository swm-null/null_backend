package com.example.oatnote.user.service;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;
import java.util.UUID;

import com.example.oatnote.user.service.model.User;

public interface UserRepository extends MongoRepository<User, UUID> {

    Optional<User> findByEmail(String email);
}
