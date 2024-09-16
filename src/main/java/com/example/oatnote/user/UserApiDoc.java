package com.example.oatnote.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.oatnote.user.dto.DispatchEmailRequest;
import com.example.oatnote.user.dto.LoginUserRequest;
import com.example.oatnote.user.dto.LoginUserResponse;
import com.example.oatnote.user.dto.RefreshUserRequest;
import com.example.oatnote.user.dto.RefreshUserResponse;
import com.example.oatnote.user.dto.RegisterUserRequest;
import com.example.oatnote.user.dto.VerifyEmailRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "User", description = "유저 관리 API")
public interface UserApiDoc {

    @Operation(summary = "회원 가입")
    @ApiResponses({
        @ApiResponse(responseCode = "201"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
    })
    @PostMapping("/user/register")
    ResponseEntity<Void> register(
        @RequestBody @Valid RegisterUserRequest registerUserRequest
    );

    @Operation(summary = "로그인")
    @ApiResponses({
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
    })
    @PostMapping("/user/login")
    ResponseEntity<LoginUserResponse> login(
        @RequestBody @Valid LoginUserRequest loginUserRequest
    );

    @Operation(summary = "액세스 토큰 갱신")
    @ApiResponses({
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
    })
    @PostMapping("/user/refresh")
    ResponseEntity<RefreshUserResponse> refreshAccessToken(
        @RequestBody @Valid RefreshUserRequest refreshUserRequest
    );

    @Operation(summary = "이메일 인증 코드 전송")
    @ApiResponses({
        @ApiResponse(responseCode = "204"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
    })
    @PostMapping("/user/email/dispatch")
    ResponseEntity<Void> dispatchEmail(
        @RequestBody @Valid DispatchEmailRequest dispatchEmailRequest
    );

    @Operation(summary = "이메일 인증 코드 확인")
    @ApiResponses({
        @ApiResponse(responseCode = "204"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
    })
    @PostMapping("/user/email/verification")
    ResponseEntity<Void> verifyEmail(
        @RequestBody @Valid VerifyEmailRequest VerifyEmailRequest
    );
}
