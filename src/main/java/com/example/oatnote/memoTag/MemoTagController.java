package com.example.oatnote.memoTag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.example.oatnote.memoTag.dto.CreateTagRequest;
import com.example.oatnote.memoTag.dto.CreateTagResponse;
import com.example.oatnote.memoTag.dto.MemosTagsResponse;
import com.example.oatnote.memoTag.dto.RootMemosTagsResponse;
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
        @RequestBody @Valid CreateMemoTagsRequest createMemoTagsRequest
    ) {
        CreateMemoTagsResponse createMemoTagsResponse = memoTagService.createMemoTags(createMemoTagsRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createMemoTagsResponse);
    }

    @PostMapping("/memos/tags/email")
    public ResponseEntity<Void> createMemosTagsByEmail(
        @RequestBody @Valid CreateMemosTagsRequest createMemosTagsRequest
    ) {
        memoTagService.createMemosTags(createMemosTagsRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @GetMapping("/memos/tags/root")
    public ResponseEntity<RootMemosTagsResponse> getRootMemosTags(
        @RequestParam(name = "page", defaultValue = "1") Integer page,
        @RequestParam(name = "limit", defaultValue = "10", required = false) Integer limit
    ) {
        RootMemosTagsResponse rootMemosTagsResponse = memoTagService.getRootMemosTags(page, limit);
        return ResponseEntity.status(HttpStatus.OK).body(rootMemosTagsResponse);
    }

    @GetMapping("/memos/tags/{tagId}")
    public ResponseEntity<ChildMemosTagsResponse> getChildMemosTags(
        @PathVariable("tagId") String tagId,
        @RequestParam(name = "page", defaultValue = "1") Integer page,
        @RequestParam(name = "limit", defaultValue = "10", required = false) Integer limit
    ) {
        ChildMemosTagsResponse childMemosTagsResponse = memoTagService.getChildMemosTags(tagId, page, limit);
        return ResponseEntity.status(HttpStatus.OK).body(childMemosTagsResponse);
    }

    @GetMapping("/memos/tag/{tagId}")
    public ResponseEntity<MemosTagsResponse> getMemosByTagId( //todo 디자인 확정되면 메모에 태그가 있는지 체크
        @PathVariable("tagId") String tagId,
        @RequestParam(name = "page", defaultValue = "1") Integer page,
        @RequestParam(name = "limit", defaultValue = "10", required = false) Integer limit
    ) {
        MemosTagsResponse memosTagsResponse = memoTagService.getMemos(tagId, page, limit);
        return ResponseEntity.status(HttpStatus.OK).body(memosTagsResponse);
    }

    @PostMapping("/memos/tags/search")
    public ResponseEntity<SearchMemoResponse> getMemosTagsByAISearch(
        @RequestBody @Valid SearchMemoRequest searchMemoRequest
    ) {
        SearchMemoResponse searchMemoResponse = memoTagService.searchMemosTags(searchMemoRequest);
        return ResponseEntity.status(HttpStatus.OK).body(searchMemoResponse);
    }

    @PutMapping("/memo/{memoId}")
    public ResponseEntity<UpdateMemoResponse> updateMemo(
        @PathVariable("memoId") String memoId,
        @RequestBody @Valid UpdateMemoRequest updateMemoRequest
    ) {
        UpdateMemoResponse updateMemoResponse = memoTagService.updateMemo(memoId, updateMemoRequest);
        return ResponseEntity.status(HttpStatus.OK).body(updateMemoResponse);
    }

    @PutMapping("/tag/{tagId}")
    public ResponseEntity<UpdateTagResponse> updateTag(
        @PathVariable("tagId") String tagId,
        @RequestBody @Valid UpdateTagRequest updateTagRequest
    ) {
        UpdateTagResponse updateTagResponse = memoTagService.updateTag(tagId, updateTagRequest);
        return ResponseEntity.status(HttpStatus.OK).body(updateTagResponse);
    }

    @DeleteMapping("/memo/{memoId}")
    public ResponseEntity<Void> deleteMemo(
        @PathVariable("memoId") String memoId
    ) {
        memoTagService.deleteMemo(memoId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/tag/{tagId}")
    public ResponseEntity<Void> deleteTag(
        @PathVariable("tagId") String tagId
    ) {
        memoTagService.deleteTag(tagId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}