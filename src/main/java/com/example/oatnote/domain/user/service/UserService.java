package com.example.oatnote.domain.user.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.oatnote.domain.user.dto.CheckEmailDuplicationRequest;
import com.example.oatnote.domain.user.dto.FindPasswordRequest;
import com.example.oatnote.domain.user.dto.LoginUserRequest;
import com.example.oatnote.domain.user.dto.LoginUserResponse;
import com.example.oatnote.domain.user.dto.RefreshUserRequest;
import com.example.oatnote.domain.user.dto.RefreshUserResponse;
import com.example.oatnote.domain.user.dto.RegisterUserRequest;
import com.example.oatnote.domain.user.dto.SendCodeRequest;
import com.example.oatnote.domain.user.dto.UpdateUserInfoRequest;
import com.example.oatnote.domain.user.dto.UpdateUserInfoResponse;
import com.example.oatnote.domain.user.dto.UserInfoResponse;
import com.example.oatnote.domain.user.dto.VerifyCodeRequest;
import com.example.oatnote.domain.user.service.email.EmailVerificationService;
import com.example.oatnote.domain.user.service.model.User;
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

    public void register(RegisterUserRequest registerUserRequest) {
        String email = registerUserRequest.email();
        String password = registerUserRequest.password();
        String confirmPassword = registerUserRequest.confirmPassword();

        log.info("회원가입 시도 / 이메일: {}", email);
        if (!Objects.equals(password, confirmPassword)) {
            throw OatInvalidPasswordException.withDetail("비밀번호가 일치하지 않습니다.");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw OatIllegalArgumentException.withDetail("이미 존재하는 이메일입니다.");
        }

        emailVerificationService.verifyCode(email, registerUserRequest.code());

        User user = registerUserRequest.toUser(passwordEncoder.encode(password), defaultProfileImageUrl);
        User createdUser = userRepository.save(user);
        eventPublisher.publishEvent(new RegisterUserEvent(createdUser.getId()));
        log.info("회원가입 완료 / 이메일: {} / 유저: {}", email, createdUser.getId());
    }

    public LoginUserResponse login(LoginUserRequest loginUserRequest) {
        String email = loginUserRequest.email();
        log.info("로그인 시도 / 이메일: {}", email);

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> OatDataNotFoundException.withDetail("이메일이 잘못 되었습니다."));

        String password = loginUserRequest.password();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw OatInvalidPasswordException.withDetail("비밀번호가 일치하지 않습니다.");
        }
        String accessToken = jwtUtil.generateAccessToken(user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        log.info("로그인 성공 / 이메일: {} / 유저: {}", email, user.getId());
        return LoginUserResponse.of(accessToken, refreshToken);
    }

    public RefreshUserResponse refreshAccessToken(RefreshUserRequest refreshUserRequest) {
        String refreshToken = refreshUserRequest.refreshToken();
        jwtUtil.validateRefreshToken(refreshToken);
        String userId = jwtUtil.extractUserId(refreshToken);
        String newAccessToken = jwtUtil.generateAccessToken(userId);

        log.info("토큰 갱신 / 유저: {}", userId);
        return RefreshUserResponse.of(newAccessToken, refreshToken);
    }

    public void checkEmailDuplication(CheckEmailDuplicationRequest checkEmailDuplicationRequest) {
        String email = checkEmailDuplicationRequest.email();
        if (userRepository.findByEmail(email).isPresent()) {
            throw OatIllegalArgumentException.withDetail("이미 존재하는 이메일입니다.", email);
        }
    }

    public void sendCode(SendCodeRequest sendCodeRequest) {
        String email = sendCodeRequest.email();
        emailVerificationService.sendCode(email, CODE_EXPIRY_MINUTES);
    }

    public void verifyCode(VerifyCodeRequest verifyCodeRequest) {
        String email = verifyCodeRequest.email();
        String code = verifyCodeRequest.code();
        emailVerificationService.verifyCode(email, code);
        emailVerificationService.insertEmailVerification(email, code, RE_CODE_EXPIRY_MINUTES);
    }

    public void findPassword(FindPasswordRequest findPasswordRequest) {
        String email = findPasswordRequest.email();
        String newPassword = findPasswordRequest.newPassword();
        String confirmPassword = findPasswordRequest.confirmPassword();
        if (!Objects.equals(newPassword, confirmPassword)) {
            throw OatInvalidPasswordException.withDetail("비밀번호가 일치하지 않습니다.");
        }

        String code = findPasswordRequest.code();
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
        log.info("회원탈퇴 / 유저: {}", userId);
        userRepository.deleteById(userId);
        eventPublisher.publishEvent(new WithdrawUserEvent(userId));
    }

    public UpdateUserInfoResponse updateUserInfo(UpdateUserInfoRequest updateUserInfoRequest, String userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> OatDataNotFoundException.withDetail("유저를 찾지 못했습니다.", userId));
        user.update(
            updateUserInfoRequest.email(),
            updateUserInfoRequest.name(),
            updateUserInfoRequest.profileImageUrl()
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

