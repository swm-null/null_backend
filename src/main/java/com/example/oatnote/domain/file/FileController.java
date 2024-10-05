package com.example.oatnote.domain.file;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.oatnote.domain.file.dto.UploadFileResponse;
import com.example.oatnote.domain.file.service.FileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FileController implements FileApiDoc {

    private final FileService fileService;

    public ResponseEntity<UploadFileResponse> uploadFile(
        @RequestParam("file") MultipartFile file,
        @AuthenticationPrincipal String userId
    ) {
        UploadFileResponse uploadFileResponse = fileService.uploadFile(file, userId);
        return ResponseEntity.status(HttpStatus.OK).body(uploadFileResponse);
    }
}
