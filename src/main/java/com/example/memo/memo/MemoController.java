package com.example.memo.memo;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.memo.memo.models.MemoRequest;
import com.example.memo.memo.models.MemoResponse;
import com.example.memo.memo.service.MemoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/memos")
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;

    @PostMapping
    public ResponseEntity<MemoResponse> createMemo(
        @RequestBody @Valid MemoRequest memoRequest
    ) {
        MemoResponse memoResponse = MemoResponse.from(
            memoService.createMemo(memoRequest.toMemoRequestBridge(memoRequest))
        );
        return new ResponseEntity<>(memoResponse, HttpStatus.CREATED);
    }

    @PostMapping("/search")
    public ResponseEntity<List<MemoResponse>> searchMemos(
        @RequestBody @Valid MemoRequest memoRequest
    ) {
        List<MemoResponse> memoResponses = memoService.searchMemo(memoRequest.toMemoRequestBridge(memoRequest))
            .stream()
            .map(MemoResponse::from)
            .collect(Collectors.toList());
        return ResponseEntity.ok(memoResponses);
    }
}
