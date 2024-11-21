package com.example.oatnote.web.validation.enums;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AllowedFileTypeEnum {
    IMAGE(Set.of("jpg", "jpeg", "png", "webp", "gif")),
    VOICE(Set.of("mp3", "mpeg", "mpga", "m4a", "wav", "webm")),
    TXT(Set.of("txt")),
    CSV(Set.of("csv"));

    private final Set<String> extensions;
}
