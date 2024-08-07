package com.example.oatnote.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.oatnote.user.models.LoginUserRequest;
import com.example.oatnote.user.models.LoginUserResponse;
import com.example.oatnote.user.models.RefreshUserRequest;
import com.example.oatnote.user.models.RefreshUserResponse;
import com.example.oatnote.user.models.RegisterUserRequest;
import com.example.oatnote.user.models.RegisterUserResponse;
import com.example.oatnote.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users/register")
    public ResponseEntity<RegisterUserResponse> register(
        @RequestBody @Valid RegisterUserRequest registerUserRequest
    ) {
        RegisterUserResponse registerUserResponse = userService.register(registerUserRequest);
        return ResponseEntity.status(HttpStatus.OK).body(registerUserResponse);
    }

    @PostMapping("/users/login")
    public ResponseEntity<LoginUserResponse> login(
        @RequestBody @Valid LoginUserRequest loginUserRequest
    ) {
        LoginUserResponse loginUserResponse = userService.login(loginUserRequest);
        return ResponseEntity.status(HttpStatus.OK).body(loginUserResponse);
    }

    @PostMapping("/users/refresh")
    public ResponseEntity<RefreshUserResponse> refreshAccessToken(@RequestBody RefreshUserRequest refreshUserRequest) {
        RefreshUserResponse refreshUserResponse = userService.refreshAccessToken(refreshUserRequest);
        return ResponseEntity.status(HttpStatus.OK).body(refreshUserResponse);
    }
}
