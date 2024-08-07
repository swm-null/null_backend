package com.example.oatnote.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.oatnote.user.exception.UserNotFoundException;
import com.example.oatnote.user.models.LoginUserResponse;
import com.example.oatnote.user.models.User;
import com.example.oatnote.user.models.UserRequest;
import com.example.oatnote.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void signUp(UserRequest userRequest) {
        User user = new User(
            userRequest.email(),
            passwordEncoder.encode(userRequest.password())
        );
        userRepository.save(user);
    }

    public LoginUserResponse login(UserRequest userRequest) {
        User user = userRepository.findByEmail(userRequest.email())
            .orElseThrow(() -> new UserNotFoundException("유저를 찾지 못했습니다: " + userRequest.email()));
        if (passwordEncoder.matches(userRequest.password(), user.getPassword())) {
            return new LoginUserResponse(
                jwtUtil.generateAccessToken(user.getEmail()),
                jwtUtil.generateRefreshToken(user.getEmail())
            );
        }
        return null;
    }
}

