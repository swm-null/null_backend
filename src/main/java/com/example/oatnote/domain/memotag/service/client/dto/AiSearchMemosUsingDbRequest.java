package com.example.oatnote.domain.memotag.service.client.dto;

public record AiSearchMemosUsingDbRequest(
    String query,
    String userId
) {

    public static AiSearchMemosUsingDbRequest of(String query, String userId) {
        return new AiSearchMemosUsingDbRequest(
            query,
            userId
        );
    }
}
