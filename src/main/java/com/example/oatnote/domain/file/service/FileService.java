package com.example.oatnote.domain.file.service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.oatnote.domain.file.dto.UploadFileResponse;
import com.example.oatnote.domain.file.dto.UploadFilesResponse;
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
        String fileUrl = uploadToS3(file, userId);
        return UploadFileResponse.of(fileUrl);
    }

    public UploadFilesResponse uploadFiles(List<MultipartFile> files, String userId) {
        List<String> fileUrls = files.stream()
            .map(file -> uploadToS3(file, userId))
            .toList();

        return UploadFilesResponse.of(fileUrls);
    }

    String uploadToS3(MultipartFile file, String userId) {
        String filePath = generateFilePath(Objects.requireNonNull(file.getOriginalFilename()), userId);

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(filePath)
                .contentType(file.getContentType())
                .contentDisposition("inline")
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
            return String.format("%s/%s", cloudFrontDomain, filePath);
        } catch (Exception e) {
            throw OatExternalServiceException.withDetail("S3 파일 업로드 실패했습니다.", filePath);
        }
    }

    String generateFilePath(String fileNameExt, String userId) {
        String[] parts = fileNameExt.split("\\.");
        String fileExt = parts.length > 1 ? parts[parts.length - 1] : "";
        return String.format("%s/%s.%s", userId, UUID.randomUUID(), fileExt);
    }
}
