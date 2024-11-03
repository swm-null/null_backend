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
    VOICE(Map.of(
        "mp3", "audio/mpeg",
        "mp4", "video/mp4",
        "mpeg", "audio/mpeg",
        "mpga", "audio/mpeg",
        "m4a", "audio/mp4",
        "wav", "audio/wav",
        "webm", "audio/webm"
    )),
    TXT(Map.of(
        "txt", "text/plain"
    )),
    CSV(Map.of(
        "csv", "text/csv"
    ));

    private final Map<String, String> fileTypes;
}
