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
        "webp", "image/webp",
        "gif", "image/gif"
    )),
    AUDIO(Map.of(
        "mp3", "audio/mpeg",
        "wav", "audio/wav"
    )),
    TXT(Map.of(
        "txt", "text/plain"
    )),
    CSV(Map.of(
        "csv", "text/csv"
    )),
    ;

    private final Map<String, String> fileTypes;
}
