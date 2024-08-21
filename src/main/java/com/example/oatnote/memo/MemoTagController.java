package com.example.oatnote.memo;

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

import com.example.oatnote.memo.models.CreateKakaoMemosRequest;
import com.example.oatnote.memo.models.CreateMemoRequest;
import com.example.oatnote.memo.models.CreateMemoResponse;
import com.example.oatnote.memo.models.CreateTagRequest;
import com.example.oatnote.memo.models.CreateTagResponse;
import com.example.oatnote.memo.models.InnerResponse.MemoResponse;
import com.example.oatnote.memo.models.SearchMemoRequest;
import com.example.oatnote.memo.models.SearchMemoResponse;
import com.example.oatnote.memo.models.InnerResponse.TagResponse;
import com.example.oatnote.memo.models.UpdateMemoRequest;
import com.example.oatnote.memo.models.UpdateMemoResponse;
import com.example.oatnote.memo.models.UpdateTagRequest;
import com.example.oatnote.memo.models.UpdateTagResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemoTagController implements MemoTagApiDoc {

    private final MemoTagService memoTagService;

    @PostMapping("/memos")
    public ResponseEntity<CreateMemoResponse> createMemo(
        @RequestBody @Valid CreateMemoRequest createMemoRequest
    ) {
        CreateMemoResponse createMemoResponse = memoTagService.createMemo(createMemoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createMemoResponse);
    }

    @PostMapping("/memos/kakao")
    public ResponseEntity<List<CreateMemoResponse>> createKakaoMemos(
        @RequestBody @Valid CreateKakaoMemosRequest createKakaoMemosRequest
    ) {
        List<CreateMemoResponse> createKakaoMemoResponses = memoTagService.createKakaoMemos(createKakaoMemosRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createKakaoMemoResponses);
    }

    @GetMapping("/memos")
    public ResponseEntity<List<MemoResponse>> getAllMemos() {
        List<MemoResponse> MemoResponses = memoTagService.getAllMemos();
        return ResponseEntity.status(HttpStatus.OK).body(MemoResponses);
    }

    @GetMapping("/memos/tags/{tagId}")
    public ResponseEntity<List<MemoResponse>> getMemosByTagId(
        @PathVariable("tagId") String tagId
    ) {
        List<MemoResponse> MemoResponses = memoTagService.getMemos(tagId);
        return ResponseEntity.status(HttpStatus.OK).body(MemoResponses);
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

    @PostMapping("memos/{memoId}/tags")
    public ResponseEntity<CreateTagResponse> createTag(
        @PathVariable("memoId") String memoId,
        @RequestBody @Valid CreateTagRequest createTagRequest
    ) {
        CreateTagResponse createTagResponse = memoTagService.createTag(memoId, createTagRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createTagResponse);
    }

    @GetMapping("/tags")
    public ResponseEntity<List<TagResponse>> getAllTags() {
        List<TagResponse> TagResponses = memoTagService.getAllTags();
        return ResponseEntity.status(HttpStatus.OK).body(TagResponses);
    }

    @GetMapping("/tags/root")
    public ResponseEntity<List<TagResponse>> getRootTags() {
        List<TagResponse> TagResponses = memoTagService.getRootTags();
        return ResponseEntity.status(HttpStatus.OK).body(TagResponses);
    }

    @GetMapping("/tags/{tagId}/childTags")
    public ResponseEntity<List<TagResponse>> getChildTags(
        @PathVariable("tagId") String tagId
    ) {
        List<TagResponse> TagResponses = memoTagService.getChildTags(tagId);
        return ResponseEntity.status(HttpStatus.OK).body(TagResponses);
    }

    @PutMapping("/tags/{tagId}")
    public ResponseEntity<UpdateTagResponse> updateTag(
        @PathVariable("tagId") String tagId,
        @RequestBody @Valid UpdateTagRequest updateTagRequest
    ) {
        UpdateTagResponse updateTagResponse = memoTagService.updateTag(tagId, updateTagRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(updateTagResponse);
    }

    @DeleteMapping("/tags/{tagId}")
    public ResponseEntity<Void> deleteTag(
        @PathVariable("tagId") String tagId
    ) {
        memoTagService.deleteTag(tagId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
