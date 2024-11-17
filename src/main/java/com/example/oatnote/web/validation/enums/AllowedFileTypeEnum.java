package com.example.oatnote.web.validation.enums;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AllowedFileTypeEnum {
    IMAGE(Map.of(
        "jpg", List.of("image/jpeg"),
        "jpeg", List.of("image/jpeg"),
        "png", List.of("image/png"),
        "webp", List.of("image/webp"),
        "gif", List.of("image/gif")
    )),
    VOICE(Map.of(
        "mp3", List.of("audio/mpeg"),
        "mpeg", List.of("audio/mpeg"),
        "mpga", List.of("audio/mpeg"),
        "m4a", List.of("audio/mp4"),
        "wav", List.of("audio/wav"),
        "webm", List.of("audio/webm", "video/webm"),
        "mp4", List.of("video/mp4"),
        "mkv", List.of("video/x-matroska")
    )),
    TXT(Map.of(
        "txt", List.of("text/plain")
    )),
    CSV(Map.of(
        "csv", List.of("text/csv")
    ));

    private final Map<String, List<String>> fileTypes;
}
