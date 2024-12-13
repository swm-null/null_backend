package com.example.oatnote.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.oatnote.user.dto.LoginUserRequest;
import com.example.oatnote.user.dto.LoginUserResponse;
import com.example.oatnote.user.dto.RefreshUserRequest;
import com.example.oatnote.user.dto.RefreshUserResponse;
import com.example.oatnote.user.dto.RegisterUserRequest;
import com.example.oatnote.user.dto.SendCodeRequest;
import com.example.oatnote.user.dto.UpdatePasswordRequest;
import com.example.oatnote.user.dto.UpdateUserInfoRequest;
import com.example.oatnote.user.dto.UpdateUserInfoResponse;
import com.example.oatnote.user.dto.UserInfoResponse;
import com.example.oatnote.user.dto.VerifyCodeRequest;
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
        return ResponseEntity.status(HttpStatus.OK).body(userService.login(request));
    }

    @Override
    @PostMapping("/user/token/refresh")
    public ResponseEntity<RefreshUserResponse> refreshAccessToken(
        @RequestBody @Valid RefreshUserRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.refreshAccessToken(request));
    }

    @Override
    @GetMapping("/users/email/exists")
    public ResponseEntity<Void> checkEmailExists(
        @RequestParam("email") String email
    ) {
        userService.checkEmailExists(email);
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
    @PostMapping("/user/email/verificationCode/verify")
    public ResponseEntity<Void> verifyCode(
        @RequestBody @Valid VerifyCodeRequest request
    ) {
        userService.verifyCode(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    @PutMapping("/user/password")
    public ResponseEntity<Void> updatePassword(
        @RequestBody @Valid UpdatePasswordRequest request
    ) {
        userService.updatePassword(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    @GetMapping("/user/me")
    public ResponseEntity<UserInfoResponse> getUserInfo(
        @AuthenticationPrincipal String userId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserInfo(userId));
    }

    @Override
    @PutMapping("/user/me")
    public ResponseEntity<UpdateUserInfoResponse> updateUserInfo(
        @RequestBody @Valid UpdateUserInfoRequest request,
        @AuthenticationPrincipal String userId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserInfo(request, userId));
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
