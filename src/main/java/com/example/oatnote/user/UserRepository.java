package com.example.oatnote.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

import com.example.oatnote.user.models.User;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);
}
