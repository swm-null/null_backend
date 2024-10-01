package com.example.oatnote.user.service;

import java.util.Objects;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.oatnote.event.RegisterUserEvent;
import com.example.oatnote.event.WithdrawUserEvent;
import com.example.oatnote.user.dto.CheckEmailRequest;
import com.example.oatnote.user.dto.FindPasswordRequest;
import com.example.oatnote.user.dto.LoginUserRequest;
import com.example.oatnote.user.dto.LoginUserResponse;
import com.example.oatnote.user.dto.RefreshUserRequest;
import com.example.oatnote.user.dto.RefreshUserResponse;
import com.example.oatnote.user.dto.RegisterUserRequest;
import com.example.oatnote.user.dto.SendCodeRequest;
import com.example.oatnote.user.dto.VerifyCodeRequest;
import com.example.oatnote.user.service.email.EmailVerificationService;
import com.example.oatnote.user.service.model.User;
import com.example.oatnote.util.JwtUtil;
import com.example.oatnote.web.exception.auth.OatInvalidPasswordException;
import com.example.oatnote.web.exception.client.OatDataNotFoundException;
import com.example.oatnote.web.exception.client.OatIllegalArgumentException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final EmailVerificationService emailVerificationService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    private static final int CODE_EXPIRY_MINUTES = 10;
    private static final int RE_CODE_EXPIRY_MINUTES = 30;

    public void register(RegisterUserRequest registerUserRequest) {
        String password = registerUserRequest.password();
        String confirmPassword = registerUserRequest.confirmPassword();
        if (!Objects.equals(password, confirmPassword)) {
            throw OatInvalidPasswordException.withDetail("비밀번호가 일치하지 않습니다.");
        }

        String email = registerUserRequest.email();
        String code = registerUserRequest.code();
        String name = registerUserRequest.name();
        if (userRepository.findByEmail(email).isPresent()) {
            throw OatIllegalArgumentException.withDetail(String.format("이미 존재하는 이메일입니다: %s", email));
        }

        emailVerificationService.verifyCode(email, code);

        User user = new User(
            email,
            passwordEncoder.encode(password),
            name
        );
        User createdUser = userRepository.save(user);
        eventPublisher.publishEvent(new RegisterUserEvent(createdUser.getId()));
    }

    public LoginUserResponse login(LoginUserRequest loginUserRequest) {
        String email = loginUserRequest.email();
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> OatDataNotFoundException.withDetail("유저를 찾지 못했습니다: " + email));

        String password = loginUserRequest.password();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw OatInvalidPasswordException.withDetail("비밀번호가 일치하지 않습니다.");
        }
        String accessToken = jwtUtil.generateAccessToken(user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());
        return LoginUserResponse.of(accessToken, refreshToken);
    }

    public RefreshUserResponse refreshAccessToken(RefreshUserRequest refreshUserRequest) {
        String refreshToken = refreshUserRequest.refreshToken();
        jwtUtil.validateRefreshToken(refreshToken);
        String email = jwtUtil.extractUserId(refreshToken);
        String newAccessToken = jwtUtil.generateAccessToken(email);
        return RefreshUserResponse.of(newAccessToken, refreshToken);
    }

    public void checkEmailDuplication(CheckEmailRequest checkEmailRequest) {
        String email = checkEmailRequest.email();
        userRepository.findByEmail(email).ifPresent(user -> {
            throw OatIllegalArgumentException.withDetail(String.format("이미 존재하는 이메일입니다: %s", email));
        });
    }

    public void sendCode(SendCodeRequest sendCodeRequest) {
        String email = sendCodeRequest.email();
        emailVerificationService.sendCode(email, CODE_EXPIRY_MINUTES);
    }

    public void verifyCode(VerifyCodeRequest verifyCodeRequest) {
        String email = verifyCodeRequest.email();
        String code = verifyCodeRequest.code();
        emailVerificationService.verifyCode(email, code);
        emailVerificationService.saveEmailVerification(email, code, RE_CODE_EXPIRY_MINUTES);
    }

    public void findPassword(FindPasswordRequest findPasswordRequest) {
        String newPassword = findPasswordRequest.newPassword();
        String confirmPassword = findPasswordRequest.confirmPassword();
        if (!Objects.equals(newPassword, confirmPassword)) {
            throw OatInvalidPasswordException.withDetail("비밀번호가 일치하지 않습니다.");
        }

        String email = findPasswordRequest.email();
        String code = findPasswordRequest.code();
        emailVerificationService.verifyCode(email, code);

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> OatDataNotFoundException.withDetail("유저를 찾지 못했습니다: " + email));

        user.updatePassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void withdraw(String userId) {
        userRepository.deleteById(userId);
        eventPublisher.publishEvent(new WithdrawUserEvent(userId));
    }
}

