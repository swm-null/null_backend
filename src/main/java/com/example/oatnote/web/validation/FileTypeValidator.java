package com.example.oatnote.web.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.stream.Stream;

import com.example.oatnote.web.validation.enums.FileTypeEnum;

public class FileTypeValidator implements ConstraintValidator<ValidFileType, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        String fileName = Objects.requireNonNull(file.getOriginalFilename()).toLowerCase();
        String[] parts = fileName.split("\\.");
        String fileExt = parts.length > 1 ? parts[parts.length - 1] : "";

        String mimeType = file.getContentType();

        return isValidFileType(fileExt, mimeType);
    }

    private boolean isValidFileType(String fileExt, String mimeType) {
        return Stream.of(FileTypeEnum.values())
            .anyMatch(type ->
                type.getExtension().equalsIgnoreCase(fileExt) && type.getMimeType().equalsIgnoreCase(mimeType)
            );
    }
}
