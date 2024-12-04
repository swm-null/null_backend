package com.example.oatnote.web.validation;

import java.util.List;
import java.util.Objects;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.example.oatnote.memotag.dto.CreateMemoRequest;
import com.example.oatnote.memotag.dto.UpdateMemoRequest;
import com.example.oatnote.memotag.dto.UpdateMemoTagsRequest;

public class MemoAtLeastOneRequiredValidator implements ConstraintValidator<MemoAtLeastOneRequired, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        if (value instanceof CreateMemoRequest request) {
            return isValidMemoRequest(request.content(), request.imageUrls(), request.voiceUrls());
        } else if (value instanceof UpdateMemoRequest request) {
            return isValidMemoRequest(request.content(), request.imageUrls(), request.voiceUrls());
        } else if (value instanceof UpdateMemoTagsRequest request) {
            return isValidMemoRequest(request.content(), request.imageUrls(), request.voiceUrls());
        }
        return false;
    }

    private boolean isValidMemoRequest(String content, List<String> imageUrls, List<String> voiceUrls) {
        return isValidContent(content) ||
            isValidUrls(imageUrls) ||
            isValidUrls(voiceUrls);
    }

    private boolean isValidContent(String content) {
        return Objects.nonNull(content) && !content.isBlank();
    }

    private boolean isValidUrls(List<String> urls) {
        return Objects.nonNull(urls) && !urls.isEmpty();
    }
}
