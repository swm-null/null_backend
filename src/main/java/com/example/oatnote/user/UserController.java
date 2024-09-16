package com.example.oatnote.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.oatnote.user.dto.DispatchEmailRequest;
import com.example.oatnote.user.dto.LoginUserRequest;
import com.example.oatnote.user.dto.LoginUserResponse;
import com.example.oatnote.user.dto.RefreshUserRequest;
import com.example.oatnote.user.dto.RefreshUserResponse;
import com.example.oatnote.user.dto.RegisterUserRequest;
import com.example.oatnote.user.dto.VerifyEmailRequest;
import com.example.oatnote.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @PostMapping("/user/email/dispatch")
    ResponseEntity<Void> dispatchEmail(
        @RequestBody @Valid DispatchEmailRequest dispatchEmailRequest
    ) {
        userService.dispatchEmail(dispatchEmailRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/user/email/verification")
    ResponseEntity<Void> verifyEmail(
        @RequestBody @Valid VerifyEmailRequest VerifyEmailRequest
    ) {
        userService.verifyEmail(VerifyEmailRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
