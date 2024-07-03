package com.example.memo.memo.service.models.bridge;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateMemoRequestBridge {

    String content;

    List<String> tags;
}
