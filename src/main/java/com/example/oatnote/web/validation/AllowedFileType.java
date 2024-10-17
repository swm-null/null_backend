package com.example.oatnote.web.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.oatnote.web.validation.enums.AllowedFileTypeEnum;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AllowedFileTypeValidator.class)
public @interface AllowedFileType {

    String message() default "지원하지 않는 파일 형식입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    AllowedFileTypeEnum[] value();
}

