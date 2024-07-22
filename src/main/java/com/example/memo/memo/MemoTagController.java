package com.example.memo.memo;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.memo.memo.models.CreateMemoRequest;
import com.example.memo.memo.models.CreateMemoResponse;
import com.example.memo.memo.models.MemoResponse;
import com.example.memo.memo.models.SearchMemoRequest;
import com.example.memo.memo.models.SearchMemoResponse;
import com.example.memo.memo.models.TagResponse;
import com.example.memo.memo.models.UpdateMemoRequest;
import com.example.memo.memo.models.UpdateMemoResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemoTagController implements MemoTagApiDoc {

    private final MemoTagService memoTagService;

    @GetMapping("/memos")
    public ResponseEntity<List<MemoResponse>> getAllMemos() {
        List<MemoResponse> MemoResponses = memoTagService.getAllMemos();
        return ResponseEntity.status(HttpStatus.OK).body(MemoResponses);
    }

    @GetMapping("/memos/tags/{tagId}")
    public ResponseEntity<List<MemoResponse>> getMemosByTagId(
        @PathVariable("tagId") String tagId
    ) {
        List<MemoResponse> MemoResponses = memoTagService.getMemosByTag(tagId);
        return ResponseEntity.status(HttpStatus.OK).body(MemoResponses);
    }

    @PostMapping("/memos")
    public ResponseEntity<CreateMemoResponse> createMemo(
        @RequestBody @Valid CreateMemoRequest createMemoRequest
    ) {
        CreateMemoResponse createMemoResponse = memoTagService.createMemo(createMemoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createMemoResponse);
    }

    @PostMapping("/memos/search")
    public ResponseEntity<SearchMemoResponse> searchMemos(
        @RequestBody @Valid SearchMemoRequest searchMemoRequest
    ) {
        SearchMemoResponse searchMemoResponse = memoTagService.searchMemo(searchMemoRequest);
        return ResponseEntity.status(HttpStatus.OK).body(searchMemoResponse);
    }

    @PutMapping("/memos/{id}")
    public ResponseEntity<UpdateMemoResponse> updateMemo(
        @PathVariable("id") String memoId,
        @RequestBody @Valid UpdateMemoRequest updateMemoRequest
    ) {
        UpdateMemoResponse updateMemoResponse = memoTagService.updateMemo(memoId, updateMemoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(updateMemoResponse);
    }

    @DeleteMapping("/memos/{id}")
    public ResponseEntity<Void> deleteMemo(
        @PathVariable("id") String memoId
    ) {
        memoTagService.deleteMemo(memoId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/tags")
    public ResponseEntity<List<TagResponse>> getAllTags() {
        List<TagResponse> TagResponses = memoTagService.getAllTags();
        return ResponseEntity.status(HttpStatus.OK).body(TagResponses);
    }
}
