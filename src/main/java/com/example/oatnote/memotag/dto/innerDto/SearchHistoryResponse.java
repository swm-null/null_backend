package com.example.oatnote.memotag.dto.innerDto;

import java.time.LocalDateTime;

import com.example.oatnote.memotag.dto.SearchMemosResponse;

public record SearchHistoryResponse(
    String searchTerm,
    LocalDateTime createdAt,
    SearchMemosResponse searchMemosResponse
) {

}
