package com.example.oatnote.user.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.oatnote.event.UserRegisteredEvent;
import com.example.oatnote.user.dto.DispatchEmailRequest;
import com.example.oatnote.user.dto.LoginUserRequest;
import com.example.oatnote.user.dto.LoginUserResponse;
import com.example.oatnote.user.dto.RefreshUserRequest;
import com.example.oatnote.user.dto.RefreshUserResponse;
import com.example.oatnote.user.dto.RegisterUserRequest;
import com.example.oatnote.user.dto.VerifyEmailRequest;
import com.example.oatnote.user.service.email.EmailVerificationService;
import com.example.oatnote.user.service.exception.UserAuthException;
import com.example.oatnote.user.service.exception.UserNotFoundException;
import com.example.oatnote.user.service.model.User;
import com.example.oatnote.util.JwtUtil;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final EmailVerificationService emailVerificationService;

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    public void register(RegisterUserRequest registerUserRequest) {
        String password = registerUserRequest.password();
        if (password == null || !password.equals(registerUserRequest.confirmPassword())) {
            throw new UserAuthException("비밀번호가 일치하지 않습니다.");
        }
        if (userRepository.findByEmail(registerUserRequest.email()).isPresent()) {
            throw new UserAuthException("이미 존재하는 이메일입니다: " + registerUserRequest.email());
        }
        User user = new User(
            registerUserRequest.email(),
            passwordEncoder.encode(registerUserRequest.password())
        );
        userRepository.save(user);
        eventPublisher.publishEvent(new UserRegisteredEvent(user.getId()));
    }

    public LoginUserResponse login(LoginUserRequest loginUserRequest) {
        User user = userRepository.findByEmail(loginUserRequest.email())
            .orElseThrow(() -> new UserNotFoundException("유저를 찾지 못했습니다: " + loginUserRequest.email()));
        if (!passwordEncoder.matches(loginUserRequest.password(), user.getPassword())) {
            throw new UserAuthException("비밀번호가 일치하지 않습니다");
        }
        String accessToken = jwtUtil.generateAccessToken(user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());
        return LoginUserResponse.of(accessToken, refreshToken);
    }

    public RefreshUserResponse refreshAccessToken(RefreshUserRequest refreshUserRequest) {
        String refreshToken = refreshUserRequest.refreshToken();
        try {
            jwtUtil.validateToken(refreshToken);
            String email = jwtUtil.extractUserId(refreshToken);
            String newAccessToken = jwtUtil.generateAccessToken(email);
            return RefreshUserResponse.of(newAccessToken, refreshToken);
        } catch (JwtException e) {
            throw new UserAuthException("refresh token 이 일치하지 않습니다." + e);
        }
    }

    public void dispatchEmail(DispatchEmailRequest dispatchEmailRequest) {
        String code = 100000 + (int)(Math.random() * 900000) + "";
        emailVerificationService.sendVerificationCode(dispatchEmailRequest.email(), code);
    }

    public void verifyEmail(VerifyEmailRequest verifyEmailRequest) {
        emailVerificationService.verifyEmail(verifyEmailRequest.email(), verifyEmailRequest.code());
    }
}

