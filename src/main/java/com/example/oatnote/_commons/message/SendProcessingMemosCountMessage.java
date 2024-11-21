package com.example.oatnote._commons.message;

public record SendProcessingMemosCountMessage(
    String userId
) {

    public static SendProcessingMemosCountMessage of(String userId) {
        return new SendProcessingMemosCountMessage(userId);
    }
}
