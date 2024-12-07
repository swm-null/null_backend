package com.example.oatnote.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping("/user/refresh")
    public ResponseEntity<RefreshUserResponse> refreshAccessToken(
        @RequestBody RefreshUserRequest request
    ) {
        RefreshUserResponse response = userService.refreshAccessToken(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @PostMapping("/user/checkEmail")
    public ResponseEntity<Void> checkEmailDuplication(
        @RequestBody @Valid CheckEmailDuplicationRequest request
    ) {
        userService.checkEmailDuplication(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    @PostMapping("/user/sendCode")
    public ResponseEntity<Void> sendCode(
        @RequestBody @Valid SendCodeRequest request
    ) {
        userService.sendCode(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    @PostMapping("/user/verifyCode")
    public ResponseEntity<Void> verifyCode(
        @RequestBody @Valid VerifyCodeRequest request
    ) {
        userService.verifyCode(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    @PostMapping("/user/findPassword")
    public ResponseEntity<Void> findPassword(
        @RequestBody @Valid FindPasswordRequest request
    ) {
        userService.findPassword(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    @PostMapping("/user/me")
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
    @DeleteMapping("/user")
    public ResponseEntity<Void> withdraw(
        @AuthenticationPrincipal String userId
    ) {
        userService.withdraw(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
