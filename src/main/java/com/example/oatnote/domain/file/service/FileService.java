package com.example.oatnote.domain.file.service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.oatnote._commons.message.DeleteFilesMessage;
import com.example.oatnote._commons.message.DeleteAllFilesMessage;
import com.example.oatnote.domain.file.dto.UploadFileResponse;
import com.example.oatnote.domain.file.dto.UploadFilesResponse;
import com.example.oatnote.web.controller.exception.server.OatExternalServiceException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

@Service
@RequiredArgsConstructor
@Slf4j
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

    public void deleteFiles(DeleteFilesMessage deleteFilesMessage) {
        List<String> fileUrls = deleteFilesMessage.fileUrls();
        String userId = deleteFilesMessage.userId();

        for (String fileUrl : fileUrls) {
            try {
                String s3Key = fileUrl.replace(cloudFrontDomain + "/", "");

                s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build());
            } catch (Exception e) {
                throw OatExternalServiceException.withDetail("S3 파일 삭제 실패했 / userId: {}", fileUrl);
            }
        }

        log.info("S3 파일 삭제 완료 / userId: {}", userId);
    }

    public void deleteAllFiles(DeleteAllFilesMessage deleteAllFilesMessage) {
        String userId = deleteAllFilesMessage.userId();
        String prefix = userId + "/";

        try {
            List<String> keysToDelete = s3Client.listObjectsV2(ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix(prefix)
                    .build())
                .contents().stream()
                .map(S3Object::key)
                .toList();

            if (!keysToDelete.isEmpty()) {
                s3Client.deleteObjects(DeleteObjectsRequest.builder()
                    .bucket(bucketName)
                    .delete(builder -> builder.objects(
                        keysToDelete.stream()
                            .map(key -> software.amazon.awssdk.services.s3.model.ObjectIdentifier.builder()
                                .key(key)
                                .build())
                            .toList()))
                    .build());
                log.info("S3 유저 전체 파일 삭제 완료 / userId: {}", userId);
            } else {
                log.info("삭제할 파일이 없습니다. / userId: {}", userId);
            }
        } catch (Exception e) {
            throw OatExternalServiceException.withDetail("S3 유저 전체 파일 삭제 실패", userId);
        }
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
        String fileExt = parts.length > 1 ? parts[parts.length - 1].toLowerCase() : "";
        return String.format("%s/%s.%s", userId, UUID.randomUUID(), fileExt);
    }
}
