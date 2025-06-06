package com.example.oatnote.file;

import static com.example.oatnote.web.validation.enums.AllowedFileTypeEnum.*;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.oatnote.file.dto.UploadFileResponse;
import com.example.oatnote.file.dto.UploadFilesResponse;
import com.example.oatnote.file.service.FileService;
import com.example.oatnote.web.validation.AllowedFileType;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
public class FileController implements FileApiDoc {

    private final FileService fileService;

    @Override
    @PostMapping(
        value = "/upload/file",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UploadFileResponse> uploadFile(
        @RequestPart @AllowedFileType({IMAGE, VOICE, TXT, CSV}) MultipartFile file,
        @AuthenticationPrincipal String userId
    ) {
        UploadFileResponse uploadFileResponse = fileService.uploadFile(file, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(uploadFileResponse);
    }

    @Override
    @PostMapping(
        value = "/upload/files",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UploadFilesResponse> uploadFiles(
        @RequestPart @AllowedFileType({IMAGE, VOICE, TXT, CSV}) List<MultipartFile> files,
        @AuthenticationPrincipal String userId
    ) {
        UploadFilesResponse uploadFilesResponse = fileService.uploadFiles(files, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(uploadFilesResponse);
    }
}
