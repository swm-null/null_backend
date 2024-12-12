package com.example.oatnote.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.oatnote.user.dto.*;

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
        @RequestBody @Valid RegisterUserRequest request
    );

    @Operation(summary = "로그인")
    @ApiResponses({
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
    })
    @PostMapping("/user/login")
    ResponseEntity<LoginUserResponse> login(
        @RequestBody @Valid LoginUserRequest request
    );

    @Operation(summary = "액세스 토큰 갱신")
    @ApiResponses({
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
    })
    @PostMapping("/user/token/refresh")
    ResponseEntity<RefreshUserResponse> refreshAccessToken(
        @RequestBody @Valid RefreshUserRequest request
    );

    @Operation(summary = "이메일 중복 체크")
    @ApiResponses({
        @ApiResponse(responseCode = "204"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
    })
    @PostMapping("/user/email/exists")
    ResponseEntity<Void> checkEmailDuplication(
        @RequestBody @Valid CheckEmailDuplicationRequest request
    );

    @Operation(summary = "이메일 인증 코드 전송")
    @ApiResponses({
        @ApiResponse(responseCode = "204"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
    })
    @PostMapping("/user/email/verificationCode")
    ResponseEntity<Void> sendCode(
        @RequestBody @Valid SendCodeRequest request
    );

    @Operation(summary = "인증 코드 검사")
    @ApiResponses({
        @ApiResponse(responseCode = "204"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
    })
    @PostMapping("/user/email/verifyCode")
    ResponseEntity<Void> verifyCode(
        @RequestBody @Valid VerifyCodeRequest request
    );

    @Operation(summary = "비밀번호 재설정")
    @ApiResponses({
        @ApiResponse(responseCode = "204"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true))),
    })
    @PostMapping("/user/password/reset")
    ResponseEntity<Void> resetPassword(
        @RequestBody @Valid FindPasswordRequest request
    );

    @Operation(summary = "유저 본인 정보 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true))),
    })
    @PostMapping("/user/me")
    ResponseEntity<UserInfoResponse> getUserInfo(
        @AuthenticationPrincipal String userId
    );

    @Operation(summary = "유저 본인 정보 수정")
    @ApiResponses({
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true))),
    })
    @PutMapping("/user/me")
    ResponseEntity<UpdateUserInfoResponse> updateUserInfo(
        @RequestBody @Valid UpdateUserInfoRequest request,
        @AuthenticationPrincipal String userId
    );

    @Operation(summary = "회원 탈퇴")
    @ApiResponses({
        @ApiResponse(responseCode = "204"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true))),
    })
    @DeleteMapping("/user/me")
    ResponseEntity<Void> withdraw(
        @AuthenticationPrincipal String userId
    );
}
