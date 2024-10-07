package com.example.oatnote.domain.file.service;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.oatnote.domain.file.dto.UploadFileResponse;
import com.example.oatnote.web.exception.server.OatExternalServiceException;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class FileService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.cloudfront.custom-domain}")
    private String cloudFrontDomain;

    public UploadFileResponse uploadFile(MultipartFile file, String userId) {
        String filePath = generateFilePath(Objects.requireNonNull(file.getOriginalFilename()), userId);

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(filePath)
                .contentType(file.getContentType())
                .contentDisposition("inline")
                .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
            String fileUrl = String.format("%s/%s", cloudFrontDomain, filePath);

            return new UploadFileResponse(fileUrl);
        } catch (Exception e) {
            throw OatExternalServiceException.withDetail("S3 파일 업로드 실패했습니다.", filePath);
        }
    }

    private String generateFilePath(String fileNameExt, String userId) {
        // 파일 이름과 확장자를 분리
        String[] parts = fileNameExt.split("\\.");
        String fileExt = parts[parts.length - 1];

        return String.format("%s/%s.%s", userId, UUID.randomUUID(), fileExt);
    }
}
