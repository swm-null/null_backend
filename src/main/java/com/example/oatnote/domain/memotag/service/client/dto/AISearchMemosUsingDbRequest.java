package com.example.oatnote.domain.memotag.service.client.dto;

public record AISearchMemosUsingDbRequest(
    String query,
    String userId
) {

    public static AISearchMemosUsingDbRequest of(String query, String userId) {
        return new AISearchMemosUsingDbRequest(
            query,
            userId
        );
    }
}
