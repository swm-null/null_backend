package com.example.oatnote.user.service;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

import com.example.oatnote.user.service.model.User;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);
}
