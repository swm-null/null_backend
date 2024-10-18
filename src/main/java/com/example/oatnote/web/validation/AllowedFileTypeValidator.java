package com.example.oatnote.web.validation;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.web.multipart.MultipartFile;

import com.example.oatnote.web.validation.enums.AllowedFileTypeEnum;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AllowedFileTypeValidator implements ConstraintValidator<AllowedFileType, Object> {

    private Set<AllowedFileTypeEnum> allowedTypes;

    @Override
    public void initialize(AllowedFileType constraintAnnotation) {
        allowedTypes = Arrays.stream(constraintAnnotation.value())
            .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (value instanceof String url) {
            return isValidExtension(getFileExtension(url));
        } else if (value instanceof MultipartFile file) {
            return isValidFile(file);
        } else if (value instanceof List<?> list) {
            return isValidList(list);
        }
        return false;
    }

    private boolean isValidFile(MultipartFile file) {
        String fileExtension = getFileExtension(file.getOriginalFilename());
        String mimeType = file.getContentType();
        return !file.isEmpty() && isValidExtension(fileExtension) && isValidMimeType(fileExtension, mimeType);
    }

    private boolean isValidList(List<?> list) {
        if (list.isEmpty()) {
            return false;
        }

        return list.stream().allMatch(item ->
            (item instanceof String && isValidExtension(getFileExtension((String)item))) ||
                (item instanceof MultipartFile && isValidFile((MultipartFile)item))
        );
    }

    private boolean isValidExtension(String fileExtension) {
        return fileExtension != null && allowedTypes.stream()
            .anyMatch(type -> type.getFileTypes().containsKey(fileExtension));
    }

    private boolean isValidMimeType(String fileExtension, String mimeType) {
        return fileExtension != null && allowedTypes.stream()
            .anyMatch(type -> {
                String allowedMimeType = type.getFileTypes().get(fileExtension);
                return allowedMimeType != null && allowedMimeType.equalsIgnoreCase(mimeType);
            });
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return null;
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
    }
}
