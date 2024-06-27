package com.example.memo.memo.service.models;

import java.util.List;

import lombok.Getter;

@Getter
public class SaveResponse {

    String memo_id;

    List<String> tags;
}
