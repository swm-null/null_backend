package com.example.oatnote.web.validation.enums;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AllowedFileTypeEnum {
    IMAGE(Map.of(
        "jpg", "image/jpeg",
        "jpeg", "image/jpeg",
        "png", "image/png",
        "svg", "image/svg+xml"
    )),
    AUDIO(Map.of(
        "mp3", "audio/mpeg",
        "wav", "audio/wav"
    )),
    TXT(Map.of(
        "txt", "text/plain"
    )),
    SVG(Map.of(
        "csv", "text/csv"
    )),
    ;

    private final Map<String, String> fileTypes;
}