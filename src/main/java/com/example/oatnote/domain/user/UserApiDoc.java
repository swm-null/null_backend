package com.example.oatnote.domain.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.oatnote.domain.user.dto.CheckEmailRequest;
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

    @Operation(summary = "이메일 중복 체크")
    @ApiResponses({
        @ApiResponse(responseCode = "204"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
    })
    @PostMapping("/user/checkEmail")
    ResponseEntity<Void> checkEmailDuplication(
        @RequestBody @Valid CheckEmailRequest checkEmailRequest
    );

    @Operation(summary = "이메일 인증 코드 전송")
    @ApiResponses({
        @ApiResponse(responseCode = "204"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
    })
    @PostMapping("/user/sendCode")
    ResponseEntity<Void> sendCode(
        @RequestBody @Valid SendCodeRequest sendCodeRequest
    );

    @Operation(summary = "이메일 인증 코드 확인")
    @ApiResponses({
        @ApiResponse(responseCode = "204"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
    })
    @PostMapping("/user/verifyCode")
    ResponseEntity<Void> verifyCode(
        @RequestBody @Valid VerifyCodeRequest verifyCodeRequest
    );

    @Operation(summary = "비밀번호 찾기")
    @ApiResponses({
        @ApiResponse(responseCode = "204"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true))),
    })
    @PostMapping("/user/findPassword")
    ResponseEntity<Void> findPassword(
        @RequestBody @Valid FindPasswordRequest findPasswordRequest
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
        @RequestBody @Valid UpdateUserInfoRequest updateUserInfoRequest,
        @AuthenticationPrincipal String userId
    );

    @Operation(summary = "회원 탈퇴")
    @ApiResponses({
        @ApiResponse(responseCode = "204"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true))),
    })
    @DeleteMapping("/user")
    ResponseEntity<Void> withdraw(
        @AuthenticationPrincipal String userId
    );
}
