package com.example.oatnote.memotag.dto;

import static com.example.oatnote.web.validation.enums.AllowedFileTypeEnum.CSV;
import static com.example.oatnote.web.validation.enums.AllowedFileTypeEnum.TXT;
import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.example.oatnote.web.validation.AllowedFileType;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@JsonNaming(SnakeCaseStrategy.class)
public record CreateMemosByFileRequest(
    @Schema(description = "파일 url", example = "https://example.com/kakao.csv", requiredMode = REQUIRED)
    @NotBlank(message = "파일 url은 비워둘 수 없습니다.")
    @AllowedFileType({TXT, CSV})
    String fileUrl,

    @Schema(description = "이메일", example = "example123@naver.com", requiredMode = NOT_REQUIRED)
    @Email(message = "유효한 이메일 주소를 입력하세요.")
    String email
) {

}
