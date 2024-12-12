package com.example.oatnote.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.oatnote.user.dto.*;
import com.example.oatnote.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApiDoc {

    private final UserService userService;

    @Override
    @PostMapping("/user/register")
    public ResponseEntity<Void> register(
        @RequestBody @Valid RegisterUserRequest request
    ) {
        userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @PostMapping("/user/login")
    public ResponseEntity<LoginUserResponse> login(
        @RequestBody @Valid LoginUserRequest request
    ) {
        LoginUserResponse response = userService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @PostMapping("/user/token/refresh")
    public ResponseEntity<RefreshUserResponse> refreshAccessToken(
        @RequestBody RefreshUserRequest request
    ) {
        RefreshUserResponse response = userService.refreshAccessToken(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @PostMapping("/user/email/exists")
    public ResponseEntity<Void> checkEmailDuplication(
        @RequestBody @Valid CheckEmailDuplicationRequest request
    ) {
        userService.checkEmailDuplication(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    @PostMapping("/user/email/verificationCode")
    public ResponseEntity<Void> sendCode(
        @RequestBody @Valid SendCodeRequest request
    ) {
        userService.sendCode(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    @PostMapping("/user/email/verifyCode")
    public ResponseEntity<Void> verifyCode(
        @RequestBody @Valid VerifyCodeRequest request
    ) {
        userService.verifyCode(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    @PostMapping("/user/password/reset")
    public ResponseEntity<Void> resetPassword(
        @RequestBody @Valid FindPasswordRequest request
    ) {
        userService.resetPassword(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    @GetMapping("/user/me")
    public ResponseEntity<UserInfoResponse> getUserInfo(
        @AuthenticationPrincipal String userId
    ) {
        UserInfoResponse response = userService.getUserInfo(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @PutMapping("/user/me")
    public ResponseEntity<UpdateUserInfoResponse> updateUserInfo(
        @RequestBody @Valid UpdateUserInfoRequest request,
        @AuthenticationPrincipal String userId
    ) {
        UpdateUserInfoResponse response = userService.updateUserInfo(request, userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @DeleteMapping("/user/me")
    public ResponseEntity<Void> withdraw(
        @AuthenticationPrincipal String userId
    ) {
        userService.withdraw(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
