package com.example.oatnote.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.oatnote.user.exception.UserNotFoundException;
import com.example.oatnote.user.models.LoginUserRequest;
import com.example.oatnote.user.models.LoginUserResponse;
import com.example.oatnote.user.models.RegisterUserRequest;
import com.example.oatnote.user.models.RegisterUserResponse;
import com.example.oatnote.user.models.User;
import com.example.oatnote.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public RegisterUserResponse register(RegisterUserRequest registerUserRequest) {
        User user = new User(
            registerUserRequest.email(),
            passwordEncoder.encode(registerUserRequest.password())
        );
        userRepository.save(user);
    }

    public LoginUserResponse login(LoginUserRequest loginUserRequest) {
        User user = userRepository.findByEmail(loginUserRequest.email())
            .orElseThrow(() -> new UserNotFoundException("유저를 찾지 못했습니다: " + loginUserRequest.email()));
        if (passwordEncoder.matches(loginUserRequest.password(), user.getPassword())) {
            return new LoginUserResponse(
                jwtUtil.generateAccessToken(user.getEmail()),
                jwtUtil.generateRefreshToken(user.getEmail())
            );
        }
        return null;
    }
}

