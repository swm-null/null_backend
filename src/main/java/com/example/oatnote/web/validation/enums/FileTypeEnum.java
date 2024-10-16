package com.example.oatnote.web.validation.enums;

import lombok.Getter;

@Getter
public enum FileTypeEnum {
    CSV("csv", "text/csv"),
    TXT("txt", "text/plain"),
    JPG("jpg", "image/jpeg"),
    JPEG("jpeg", "image/jpeg"),
    SVG("svg", "image/svg+xml"),
    PNG("png", "image/png"),
    MP3("mp3", "audio/mpeg"),
    WAV("wav", "audio/wav");

    private final String extension;
    private final String mimeType;

    FileTypeEnum(String extension, String mimeType) {
        this.extension = extension;
        this.mimeType = mimeType;
    }
}
