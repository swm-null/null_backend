package com.example.oatnote.user.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.oatnote.user.models.LoginUserRequest;
import com.example.oatnote.user.models.LoginUserResponse;
import com.example.oatnote.user.models.RefreshUserRequest;
import com.example.oatnote.user.models.RefreshUserResponse;
import com.example.oatnote.user.models.RegisterUserRequest;
import com.example.oatnote.user.service.exception.AuthIllegalArgumentException;
import com.example.oatnote.user.service.exception.UserNotFoundException;
import com.example.oatnote.user.service.models.User;
import com.example.oatnote.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void register(RegisterUserRequest registerUserRequest) {
        if(userRepository.findByEmail(registerUserRequest.email()).isPresent()) {
            throw new AuthIllegalArgumentException("이미 존재하는 이메일입니다: " + registerUserRequest.email());
        }
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
            String accessToken = jwtUtil.generateAccessToken(user.getEmail());
            String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());
            return LoginUserResponse.of(accessToken, refreshToken);
        } else {
            throw new AuthIllegalArgumentException("비밀번호가 일치하지 않습니다");
        }
    }

    public RefreshUserResponse refreshAccessToken(RefreshUserRequest refreshUserRequest) {
        String refreshToken = refreshUserRequest.refreshToken();
        if (jwtUtil.validateToken(refreshToken)) {
            String email = jwtUtil.extractEmail(refreshToken);
            String newAccessToken = jwtUtil.generateAccessToken(email);
            return RefreshUserResponse.of(newAccessToken, refreshToken);
        } else {
            throw new AuthIllegalArgumentException("refresh token 이 일치하지 않습니다.");
        }
    }
}

