package com.example.oatnote.web.validation;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.web.multipart.MultipartFile;

import com.example.oatnote.web.validation.enums.AllowedFileTypeEnum;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AllowedFileTypeValidator implements ConstraintValidator<AllowedFileType, Object> {

    private Set<String> allowedExtensions;

    @Override
    public void initialize(AllowedFileType constraintAnnotation) {
        allowedExtensions = Arrays.stream(constraintAnnotation.value())
            .flatMap(type -> type.getExtensions().stream())
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
        return !file.isEmpty() && isValidExtension(fileExtension);
    }

    private boolean isValidList(List<?> list) {
        if (list.isEmpty()) {
            return true;
        }

        return list.stream().allMatch(item ->
            (item instanceof String && isValidExtension(getFileExtension((String) item))) ||
                (item instanceof MultipartFile && isValidFile((MultipartFile) item))
        );
    }

    private boolean isValidExtension(String fileExtension) {
        return fileExtension != null && allowedExtensions.contains(fileExtension.toLowerCase());
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return null;
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
    }
}
