package com.example.oatnote.web.validation;

import java.util.Objects;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.example.oatnote.domain.memotag.dto.CreateMemoRequest;

public class MemoAtLeastOneRequiredValidator implements ConstraintValidator<MemoAtLeastOneRequired, CreateMemoRequest> {

    @Override
    public boolean isValid(CreateMemoRequest request, ConstraintValidatorContext context) {
        return (Objects.nonNull(request.content()) && !request.content().isBlank()) ||
            (Objects.nonNull(request.imageUrls()) && !request.imageUrls().isEmpty()) ||
            (Objects.nonNull(request.voiceUrls()) && !request.voiceUrls().isEmpty());
    }
}

