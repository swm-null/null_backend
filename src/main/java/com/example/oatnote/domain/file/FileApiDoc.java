package com.example.oatnote.domain.file;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.example.oatnote.domain.file.dto.UploadFileResponse;
import com.example.oatnote.domain.file.dto.UploadFilesResponse;
import com.example.oatnote.web.validation.ValidFileType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Upload File", description = "파일 업로드 API")
@Validated
public interface FileApiDoc {

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(hidden = true))),
        }
    )
    @Operation(summary = "파일 업로드")
    @PostMapping(
        value = "/upload/file",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<UploadFileResponse> uploadFile(
        @RequestPart @ValidFileType MultipartFile file,
        @AuthenticationPrincipal String userId
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(hidden = true))),
        }
    )
    @Operation(summary = "파일 리스트 업로드")
    @PostMapping(
        value = "/upload/files",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<UploadFilesResponse> uploadFiles(
        @RequestPart List<@ValidFileType MultipartFile> files,
        @AuthenticationPrincipal String userId
    );
}
