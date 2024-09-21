package com.example.oatnote.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.oatnote.user.dto.CheckEmailRequest;
import com.example.oatnote.user.dto.SendCodeRequest;
import com.example.oatnote.user.dto.FindPasswordRequest;
import com.example.oatnote.user.dto.LoginUserRequest;
import com.example.oatnote.user.dto.LoginUserResponse;
import com.example.oatnote.user.dto.RefreshUserRequest;
import com.example.oatnote.user.dto.RefreshUserResponse;
import com.example.oatnote.user.dto.RegisterUserRequest;
import com.example.oatnote.user.dto.VerifyCodeRequest;
import com.example.oatnote.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApiDoc {

    private final UserService userService;

    @PostMapping("/user/register")
    public ResponseEntity<Void> register(
        @RequestBody @Valid RegisterUserRequest registerUserRequest
    ) {
        userService.register(registerUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/user/login")
    public ResponseEntity<LoginUserResponse> login(
        @RequestBody @Valid LoginUserRequest loginUserRequest
    ) {
        LoginUserResponse loginUserResponse = userService.login(loginUserRequest);
        return ResponseEntity.status(HttpStatus.OK).body(loginUserResponse);
    }

    @PostMapping("/user/refresh")
    public ResponseEntity<RefreshUserResponse> refreshAccessToken(
        @RequestBody RefreshUserRequest refreshUserRequest
    ) {
        RefreshUserResponse refreshUserResponse = userService.refreshAccessToken(refreshUserRequest);
        return ResponseEntity.status(HttpStatus.OK).body(refreshUserResponse);
    }

    @PostMapping("/user/checkEmail")
    public ResponseEntity<Void> checkEmailDuplication(
        @RequestBody @Valid CheckEmailRequest checkEmailRequest
    ) {
        userService.checkEmailDuplication(checkEmailRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/user/sendCode")
    public ResponseEntity<Void> sendCode(
        @RequestBody @Valid SendCodeRequest sendCodeRequest
    ) {
        userService.sendCode(sendCodeRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/user/verifyCode")
    public ResponseEntity<Void> verifyCode(
        @RequestBody @Valid VerifyCodeRequest verifyCodeRequest
    ) {
        userService.verifyCode(verifyCodeRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/user/findPassword")
    public ResponseEntity<Void> findPassword(
        @RequestBody @Valid FindPasswordRequest findPasswordRequest
    ) {
        userService.findPassword(findPasswordRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/user")
    public ResponseEntity<Void> withdraw(
        @AuthenticationPrincipal String userId
    ) {
        userService.withdraw(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
