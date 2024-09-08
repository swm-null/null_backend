package com.example.oatnote.memoTag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.oatnote.memoTag.dto.ChildMemosTagsResponse;
import com.example.oatnote.memoTag.dto.CreateMemoTagsRequest;
import com.example.oatnote.memoTag.dto.CreateMemoTagsResponse;
import com.example.oatnote.memoTag.dto.CreateMemosTagsRequest;
import com.example.oatnote.memoTag.dto.PagedMemosTagsResponse;
import com.example.oatnote.memoTag.dto.SearchMemoRequest;
import com.example.oatnote.memoTag.dto.SearchMemoResponse;
import com.example.oatnote.memoTag.dto.UpdateMemoRequest;
import com.example.oatnote.memoTag.dto.UpdateMemoResponse;
import com.example.oatnote.memoTag.dto.UpdateTagRequest;
import com.example.oatnote.memoTag.dto.UpdateTagResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemoTagController implements MemoTagApiDoc {

    private final MemoTagService memoTagService;

    @PostMapping("/memo/tags")
    public ResponseEntity<CreateMemoTagsResponse> createMemoTags(
        @RequestBody @Valid CreateMemoTagsRequest createMemoTagsRequest,
        @AuthenticationPrincipal String userId
    ) {
        CreateMemoTagsResponse createMemoTagsResponse = memoTagService.createMemoTags(createMemoTagsRequest, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createMemoTagsResponse);
    }

    @PostMapping("/memos/tags/email")
    public ResponseEntity<Void> createMemosTagsByEmail(
        @RequestBody @Valid CreateMemosTagsRequest createMemosTagsRequest
    ) {
        memoTagService.createMemosTags(createMemosTagsRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @GetMapping("/memos/tags/{parentTagName}")
    public ResponseEntity<ChildMemosTagsResponse> getChildMemosTags(
        @PathVariable("parentTagName") String parentTagName,
        @RequestParam(name = "tagPage", defaultValue = "1") Integer tagPage,
        @RequestParam(name = "tagLimit", defaultValue = "10") Integer tagLimit,
        @RequestParam(name = "memoPage", defaultValue = "1") Integer memoPage,
        @RequestParam(name = "memoLimit", defaultValue = "10") Integer memoLimit,
        @AuthenticationPrincipal String userId
    ) {
        ChildMemosTagsResponse childMemosTagsResponse = memoTagService.getChildMemosTags(
            parentTagName,
            tagPage,
            tagLimit,
            memoPage,
            memoLimit,
            userId
        );
        return ResponseEntity.status(HttpStatus.OK).body(childMemosTagsResponse);
    }

    @GetMapping("/memos/tag/{tagId}")
    public ResponseEntity<PagedMemosTagsResponse> getMemosByTagId(
        @PathVariable("tagId") String tagId,
        @RequestParam(name = "memoPage", defaultValue = "1") Integer memoPage,
        @RequestParam(name = "memoLimit", defaultValue = "10") Integer memoLimit,
        @AuthenticationPrincipal String userId
    ) {
        PagedMemosTagsResponse pagedMemosTagsResponse = memoTagService.getMemos(tagId, memoPage, memoLimit, userId);
        return ResponseEntity.status(HttpStatus.OK).body(pagedMemosTagsResponse);
    }

    @PostMapping("/memos/tags/search")
    public ResponseEntity<SearchMemoResponse> getMemosTagsByAISearch(
        @RequestBody @Valid SearchMemoRequest searchMemoRequest,
        @AuthenticationPrincipal String userId
    ) {
        SearchMemoResponse searchMemoResponse = memoTagService.searchMemosTags(searchMemoRequest, userId);
        return ResponseEntity.status(HttpStatus.OK).body(searchMemoResponse);
    }

    @PutMapping("/memo/{memoId}")
    public ResponseEntity<UpdateMemoResponse> updateMemo(
        @PathVariable("memoId") String memoId,
        @RequestBody @Valid UpdateMemoRequest updateMemoRequest,
        @AuthenticationPrincipal String userId
    ) {
        UpdateMemoResponse updateMemoResponse = memoTagService.updateMemo(memoId, updateMemoRequest, userId);
        return ResponseEntity.status(HttpStatus.OK).body(updateMemoResponse);
    }

    @PutMapping("/tag/{tagId}")
    public ResponseEntity<UpdateTagResponse> updateTag(
        @PathVariable("tagId") String tagId,
        @RequestBody @Valid UpdateTagRequest updateTagRequest,
        @AuthenticationPrincipal String userId
    ) {
        UpdateTagResponse updateTagResponse = memoTagService.updateTag(tagId, updateTagRequest, userId);
        return ResponseEntity.status(HttpStatus.OK).body(updateTagResponse);
    }

    @DeleteMapping("/memo/{memoId}")
    public ResponseEntity<Void> deleteMemo(
        @PathVariable("memoId") String memoId,
        @AuthenticationPrincipal String userId
    ) {
        memoTagService.deleteMemo(memoId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/tag/{tagId}")
    public ResponseEntity<Void> deleteTag(
        @PathVariable("tagId") String tagId,
        @AuthenticationPrincipal String userId
    ) {
        memoTagService.deleteTag(tagId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
