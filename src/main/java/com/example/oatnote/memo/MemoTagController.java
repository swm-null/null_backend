package com.example.oatnote.memo;

import java.util.List;

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

import com.example.oatnote.memo.dto.CreateChildTagRequest;
import com.example.oatnote.memo.dto.CreateChildTagResponse;
import com.example.oatnote.memo.dto.CreateMemoRequest;
import com.example.oatnote.memo.dto.CreateMemoResponse;
import com.example.oatnote.memo.dto.CreateMemosRequest;
import com.example.oatnote.memo.dto.CreateSearchHistoryRequest;
import com.example.oatnote.memo.dto.CreateSearchHistoryResponse;
import com.example.oatnote.memo.dto.MemosResponse;
import com.example.oatnote.memo.dto.SearchHistoriesResponse;
import com.example.oatnote.memo.dto.SearchMemosUsingAiResponse;
import com.example.oatnote.memo.dto.SearchMemosUsingDbResponse;
import com.example.oatnote.memo.dto.TagsResponse;
import com.example.oatnote.memo.dto.UpdateMemoRequest;
import com.example.oatnote.memo.dto.UpdateMemoResponse;
import com.example.oatnote.memo.dto.UpdateMemoTagsRequest;
import com.example.oatnote.memo.dto.UpdateMemoTagsResponse;
import com.example.oatnote.memo.dto.UpdateTagRequest;
import com.example.oatnote.memo.dto.UpdateTagResponse;
import com.example.oatnote.memo.dto.enums.MemoSortOrderTypeEnum;
import com.example.oatnote.memo.dto.innerDto.TagResponse;
import com.example.oatnote.memo.service.MemoTagService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemoTagController implements MemoTagApiDoc {

    private final MemoTagService memoTagService;

    @PostMapping("/memo")
    public ResponseEntity<CreateMemoResponse> createMemo(
        @RequestBody @Valid CreateMemoRequest request,
        @AuthenticationPrincipal String userId
    ) {
        CreateMemoResponse response = memoTagService.createMemo(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/linkedMemo")
    public ResponseEntity<CreateMemoResponse> createLinkedMemo(
        @RequestParam(value = "tagId", required = false) String tagId,
        @RequestBody @Valid CreateMemoRequest request,
        @AuthenticationPrincipal String userId
    ) {
        CreateMemoResponse response = memoTagService.createLinkedMemo(tagId, request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/memos")
    public ResponseEntity<Void> createMemos(
        @RequestBody @Valid CreateMemosRequest request,
        @AuthenticationPrincipal String userId
    ) {
        memoTagService.createMemos(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/childTag")
    public ResponseEntity<CreateChildTagResponse> createChildTag(
        @RequestParam(value = "tagId", required = false) String tagId,
        @RequestBody @Valid CreateChildTagRequest request,
        @AuthenticationPrincipal String userId
    ) {
        CreateChildTagResponse response = memoTagService.createChildTag(tagId, request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/memos/search/history")
    public ResponseEntity<CreateSearchHistoryResponse> createSearchHistory(
        @RequestBody @Valid CreateSearchHistoryRequest request,
        @AuthenticationPrincipal String userId
    ) {
        CreateSearchHistoryResponse response = memoTagService.createSearchHistory(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/ancestorTags")
    public ResponseEntity<List<TagResponse>> getAncestorTags(
        @RequestParam(value = "tagId") String tagId,
        @AuthenticationPrincipal String userId
    ) {
        List<TagResponse> response = memoTagService.getAncestorTags(tagId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/childTags")
    public ResponseEntity<List<TagResponse>> getChildTags(
        @RequestParam(value = "tagId", required = false) String tagId,
        @AuthenticationPrincipal String userId
    ) {
        List<TagResponse> response = memoTagService.getChildTags(tagId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/tags")
    public ResponseEntity<TagsResponse> getTags(
        @RequestParam(value = "tagId", required = false) String tagId,
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "limit", defaultValue = "10") Integer limit,
        @AuthenticationPrincipal String userId
    ) {
        TagsResponse response = memoTagService.getTags(tagId, page, limit, userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/tag/memos")
    public ResponseEntity<MemosResponse> getMemos(
        @RequestParam(value = "tagId", required = false) String tagId,
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "limit", defaultValue = "10") Integer limit,
        @RequestParam(value = "sortOrder") MemoSortOrderTypeEnum sortOrder,
        @RequestParam(value = "isLinked", required = false) Boolean isLinked,
        @AuthenticationPrincipal String userId
    ) {
        MemosResponse response = memoTagService.getMemos(tagId, page, limit, sortOrder, isLinked, userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/memos/search/ai")
    public ResponseEntity<SearchMemosUsingAiResponse> searchMemosUsingAi(
        @RequestParam(value = "searchHistoryId") String searchHistoryId,
        @AuthenticationPrincipal String userId
    ) {
        SearchMemosUsingAiResponse response = memoTagService.searchMemosUsingAi(searchHistoryId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/memos/search/db")
    public ResponseEntity<SearchMemosUsingDbResponse> searchMemosUsingDb(
        @RequestParam(value = "searchHistoryId") String searchHistoryId,
        @AuthenticationPrincipal String userId
    ) {
        SearchMemosUsingDbResponse response = memoTagService.searchMemosUsingDb(searchHistoryId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/memos/search/histories")
    public ResponseEntity<SearchHistoriesResponse> getSearchHistories(
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "limit", defaultValue = "15") Integer limit,
        @AuthenticationPrincipal String userId
    ) {
        SearchHistoriesResponse response = memoTagService.getSearchHistories(page, limit, userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/memo/{memoId}")
    public ResponseEntity<UpdateMemoResponse> updateMemo(
        @PathVariable("memoId") String memoId,
        @RequestBody @Valid UpdateMemoRequest request,
        @AuthenticationPrincipal String userId
    ) {
        UpdateMemoResponse response = memoTagService.updateMemo(memoId, request, userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/tag/{tagId}")
    public ResponseEntity<UpdateTagResponse> updateTag(
        @PathVariable("tagId") String tagId,
        @RequestBody @Valid UpdateTagRequest request,
        @AuthenticationPrincipal String userId
    ) {
        UpdateTagResponse response = memoTagService.updateTag(tagId, request, userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/memo/{memoId}/tags")
    public ResponseEntity<UpdateMemoTagsResponse> updateMemoTags(
        @PathVariable("memoId") String memoId,
        @RequestBody @Valid UpdateMemoTagsRequest request,
        @AuthenticationPrincipal String userId
    ) {
        UpdateMemoTagsResponse response = memoTagService.updateMemoTags(memoId, request, userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
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
