package com.example.oatnote.user.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.oatnote.user.dto.CheckEmailDuplicationRequest;
import com.example.oatnote.user.dto.FindPasswordRequest;
import com.example.oatnote.user.dto.LoginUserRequest;
import com.example.oatnote.user.dto.LoginUserResponse;
import com.example.oatnote.user.dto.RefreshUserRequest;
import com.example.oatnote.user.dto.RefreshUserResponse;
import com.example.oatnote.user.dto.RegisterUserRequest;
import com.example.oatnote.user.dto.SendCodeRequest;
import com.example.oatnote.user.dto.UpdateUserInfoRequest;
import com.example.oatnote.user.dto.UpdateUserInfoResponse;
import com.example.oatnote.user.dto.UserInfoResponse;
import com.example.oatnote.user.dto.VerifyCodeRequest;
import com.example.oatnote.user.service.email.EmailVerificationService;
import com.example.oatnote.user.service.model.User;
import com.example.oatnote._commons.event.RegisterUserEvent;
import com.example.oatnote._commons.event.WithdrawUserEvent;
import com.example.oatnote._commons.util.JwtUtil;
import com.example.oatnote.web.controller.exception.auth.OatInvalidPasswordException;
import com.example.oatnote.web.controller.exception.client.OatDataNotFoundException;
import com.example.oatnote.web.controller.exception.client.OatIllegalArgumentException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final EmailVerificationService emailVerificationService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${aws.cloudfront.default-profile-image-url}")
    private String defaultProfileImageUrl;

    private static final int CODE_EXPIRY_MINUTES = 10;
    private static final int RE_CODE_EXPIRY_MINUTES = 30;

    public void register(RegisterUserRequest request) {
        String email = request.email();
        String password = request.password();
        String confirmPassword = request.confirmPassword();

        if (!Objects.equals(password, confirmPassword)) {
            throw OatInvalidPasswordException.withDetail("비밀번호가 일치하지 않습니다.");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw OatIllegalArgumentException.withDetail("이미 존재하는 이메일입니다.");
        }
        emailVerificationService.verifyCode(email, request.code());

        User user = request.toUser(passwordEncoder.encode(password), defaultProfileImageUrl);
        User createdUser = userRepository.save(user);
        eventPublisher.publishEvent(new RegisterUserEvent(createdUser.getId()));
        log.info("회원가입 완료 / 이메일: {} / 유저: {}", email, createdUser.getId());
    }

    public LoginUserResponse login(LoginUserRequest request) {
        String email = request.email();
        String password = request.password();

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> OatDataNotFoundException.withDetail("이메일이 잘못 되었습니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw OatInvalidPasswordException.withDetail("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtUtil.generateAccessToken(user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());
        log.info("로그인 성공 / 이메일: {} / 유저: {}", email, user.getId());
        return LoginUserResponse.of(accessToken, refreshToken);
    }

    public RefreshUserResponse refreshAccessToken(RefreshUserRequest request) {
        String refreshToken = request.refreshToken();
        jwtUtil.validateRefreshToken(refreshToken);
        String userId = jwtUtil.extractUserId(refreshToken);
        String newAccessToken = jwtUtil.generateAccessToken(userId);

        log.info("토큰 갱신 / 유저: {}", userId);
        return RefreshUserResponse.of(newAccessToken, refreshToken);
    }

    public void checkEmailDuplication(CheckEmailDuplicationRequest request) {
        String email = request.email();
        if (userRepository.findByEmail(email).isPresent()) {
            throw OatIllegalArgumentException.withDetail("이미 존재하는 이메일입니다.", email);
        }
    }

    public void sendCode(SendCodeRequest request) {
        emailVerificationService.sendCode(request.email(), CODE_EXPIRY_MINUTES);
    }

    public void verifyCode(VerifyCodeRequest request) {
        String email = request.email();
        String code = request.code();
        emailVerificationService.verifyCode(email, code);
        emailVerificationService.insertEmailVerification(email, code, RE_CODE_EXPIRY_MINUTES);
    }

    public void resetPassword(FindPasswordRequest request) {
        String email = request.email();
        String newPassword = request.newPassword();
        String confirmPassword = request.confirmPassword();
        if (!Objects.equals(newPassword, confirmPassword)) {
            throw OatInvalidPasswordException.withDetail("비밀번호가 일치하지 않습니다.");
        }

        String code = request.code();
        emailVerificationService.verifyCode(email, code);

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> OatDataNotFoundException.withDetail("존재하지 않는 유저입니다."));
        user.updatePassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("비밀번호 찾기 후 변경 / 유저: {}", user.getId());
    }

    public UserInfoResponse getUserInfo(String userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> OatDataNotFoundException.withDetail("존재하지 않는 유저입니다.", userId));
        return UserInfoResponse.from(user);
    }

    public void withdraw(String userId) {
        userRepository.deleteById(userId);
        eventPublisher.publishEvent(new WithdrawUserEvent(userId));
        log.info("회원탈퇴 / 유저: {}", userId);
    }

    public UpdateUserInfoResponse updateUserInfo(UpdateUserInfoRequest request, String userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> OatDataNotFoundException.withDetail("유저를 찾지 못했습니다.", userId));
        user.update(
            request.email(),
            request.name(),
            request.profileImageUrl()
        );
        User updatedUser = userRepository.save(user);
        return UpdateUserInfoResponse.from(updatedUser);
    }

    public String getUserIdByEmail(String email) { //todo msa refactor
        return userRepository.findByEmail(email)
            .map(User::getId)
            .orElseThrow(() -> OatDataNotFoundException.withDetail("유저를 찾지 못했습니다.", email));
    }
}
