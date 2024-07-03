package com.example.memo.memo;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.memo.memo.models.MemoRequest;
import com.example.memo.memo.models.MemoResponse;
import com.example.memo.memo.models.UpdateMemoRequest;
import com.example.memo.memo.models.UpdateMemoResponse;
import com.example.memo.memo.service.MemoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemoController implements MemoApi {

    private final MemoService memoService;

    @PostMapping("/memos")
    public ResponseEntity<MemoResponse> createMemo(
        @RequestBody @Valid MemoRequest memoRequest
    ) {
        MemoResponse memoResponse = MemoResponse.from(memoService.createMemo(memoRequest.toMemoRequestBridge()));
        return ResponseEntity.status(HttpStatus.CREATED).body(memoResponse);
    }

    @PostMapping("/memos/search")
    public ResponseEntity<List<MemoResponse>> searchMemos(
        @RequestBody @Valid MemoRequest memoRequest
    ) {
        List<MemoResponse> memoResponses = memoService.searchMemo(memoRequest.toMemoRequestBridge())
            .stream()
            .map(MemoResponse::from)
            .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(memoResponses);
    }

    @PutMapping("/memos/{id}")
    public ResponseEntity<UpdateMemoResponse> updateMemo(
        @PathVariable("id") String memoId,
        @RequestBody @Valid UpdateMemoRequest updateMemoRequest
    ) {
        UpdateMemoResponse updateMemoResponse = UpdateMemoResponse.from(
            memoService.updateMemo(memoId, updateMemoRequest.toUpdateMemoRequestBridge())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(updateMemoResponse);
    }

    @DeleteMapping("/memos/{id}")
    public ResponseEntity<Void> deleteMemo(
        @PathVariable("id") String memoId
    ) {
        memoService.deleteMemo(memoId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
