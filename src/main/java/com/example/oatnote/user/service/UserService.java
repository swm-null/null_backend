package com.example.oatnote.user.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.oatnote.user.dto.LoginUserRequest;
import com.example.oatnote.user.dto.LoginUserResponse;
import com.example.oatnote.user.dto.RefreshUserRequest;
import com.example.oatnote.user.dto.RefreshUserResponse;
import com.example.oatnote.user.dto.RegisterUserRequest;
import com.example.oatnote.user.service.exception.AuthIllegalArgumentException;
import com.example.oatnote.user.service.exception.UserIllegalArgumentException;
import com.example.oatnote.user.service.exception.UserNotFoundException;
import com.example.oatnote.user.service.model.User;
import com.example.oatnote.util.JwtUtil;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void register(RegisterUserRequest registerUserRequest) {
        if (userRepository.findByEmail(registerUserRequest.email()).isPresent()) {
            throw new UserIllegalArgumentException("이미 존재하는 이메일입니다: " + registerUserRequest.email());
        }
        if (!registerUserRequest.isPasswordMatching()) {
            throw new UserIllegalArgumentException("비밀번호가 일치하지 않습니다.");
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
        if (!passwordEncoder.matches(loginUserRequest.password(), user.getPassword())) {
            throw new AuthIllegalArgumentException("비밀번호가 일치하지 않습니다");
        }
        String accessToken = jwtUtil.generateAccessToken(user.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());
        return LoginUserResponse.of(accessToken, refreshToken);
    }

    public RefreshUserResponse refreshAccessToken(RefreshUserRequest refreshUserRequest) {
        String refreshToken = refreshUserRequest.refreshToken();
        try {
            jwtUtil.validateToken(refreshToken);
            String email = jwtUtil.extractEmail(refreshToken);
            String newAccessToken = jwtUtil.generateAccessToken(email);
            return RefreshUserResponse.of(newAccessToken, refreshToken);
        } catch (JwtException e) {
            throw new AuthIllegalArgumentException("refresh token 이 일치하지 않습니다." + e);
        }
    }
}

