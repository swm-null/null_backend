package com.example.oatnote.memotag;

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

import com.example.oatnote.memotag.dto.CreateMemoRequest;
import com.example.oatnote.memotag.dto.CreateMemoResponse;
import com.example.oatnote.memotag.dto.CreateMemosByFileRequest;
import com.example.oatnote.memotag.dto.CreateSearchHistoryRequest;
import com.example.oatnote.memotag.dto.CreateSearchHistoryResponse;
import com.example.oatnote.memotag.dto.MemosResponse;
import com.example.oatnote.memotag.dto.SearchHistoriesResponse;
import com.example.oatnote.memotag.dto.SearchMemosUsingAiResponse;
import com.example.oatnote.memotag.dto.SearchMemosUsingDbResponse;
import com.example.oatnote.memotag.dto.UpdateMemoRequest;
import com.example.oatnote.memotag.dto.UpdateMemoResponse;
import com.example.oatnote.memotag.dto.UpdateMemoTagsRequest;
import com.example.oatnote.memotag.dto.UpdateMemoTagsResponse;
import com.example.oatnote.memotag.dto.enums.MemoSortOrderTypeEnum;
import com.example.oatnote.memotag.service.MemoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemoController implements MemoApiDoc {

    private final MemoService memoService;

    @Override
    @PostMapping("/memo")
    public ResponseEntity<CreateMemoResponse> createMemo(
        @RequestBody @Valid CreateMemoRequest request,
        @AuthenticationPrincipal String userId
    ) {
        CreateMemoResponse response = memoService.createMemo(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @PostMapping("/memo/tag")
    public ResponseEntity<CreateMemoResponse> createMemoLinkedTag(
        @RequestParam(value = "tagId", required = false) String tagId,
        @RequestBody @Valid CreateMemoRequest request,
        @AuthenticationPrincipal String userId
    ) {
        CreateMemoResponse response = memoService.createLinkedMemo(tagId, request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @PostMapping("/memos/file")
    public ResponseEntity<Void> createMemosByFile(
        @RequestBody @Valid CreateMemosByFileRequest request,
        @AuthenticationPrincipal String userId
    ) {
        memoService.createMemosByFile(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @PostMapping("/memos/search/history")
    public ResponseEntity<CreateSearchHistoryResponse> createSearchHistory(
        @RequestBody @Valid CreateSearchHistoryRequest request,
        @AuthenticationPrincipal String userId
    ) {
        CreateSearchHistoryResponse response = memoService.createSearchHistory(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @GetMapping("/memos/tag")
    public ResponseEntity<MemosResponse> getMemosLinkedTag(
        @RequestParam(value = "tagId", required = false) String tagId,
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "limit", defaultValue = "10") Integer limit,
        @RequestParam(value = "sortOrder") MemoSortOrderTypeEnum sortOrder,
        @RequestParam(value = "isLinked", required = false) Boolean isLinked,
        @AuthenticationPrincipal String userId
    ) {
        MemosResponse response = memoService.getMemosLinkedTag(tagId, page, limit, sortOrder, isLinked, userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @GetMapping("/memos/search/ai")
    public ResponseEntity<SearchMemosUsingAiResponse> searchMemosUsingAi(
        @RequestParam(value = "searchHistoryId") String searchHistoryId,
        @AuthenticationPrincipal String userId
    ) {
        SearchMemosUsingAiResponse response = memoService.searchMemosUsingAi(searchHistoryId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @GetMapping("/memos/search/db")
    public ResponseEntity<SearchMemosUsingDbResponse> searchMemosUsingDb(
        @RequestParam(value = "searchHistoryId") String searchHistoryId,
        @AuthenticationPrincipal String userId
    ) {
        SearchMemosUsingDbResponse response = memoService.searchMemosUsingDb(searchHistoryId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @GetMapping("/memos/search/histories")
    public ResponseEntity<SearchHistoriesResponse> getSearchHistories(
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "limit", defaultValue = "15") Integer limit,
        @AuthenticationPrincipal String userId
    ) {
        SearchHistoriesResponse response = memoService.getSearchHistories(page, limit, userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @PutMapping("/memo/{id}")
    public ResponseEntity<UpdateMemoResponse> updateMemo(
        @PathVariable("id") String id,
        @RequestBody @Valid UpdateMemoRequest request,
        @AuthenticationPrincipal String userId
    ) {
        UpdateMemoResponse response = memoService.updateMemo(id, request, userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @PutMapping("/memo/{id}/tags")
    public ResponseEntity<UpdateMemoTagsResponse> updateMemoTags(
        @PathVariable("id") String id,
        @RequestBody @Valid UpdateMemoTagsRequest request,
        @AuthenticationPrincipal String userId
    ) {
        UpdateMemoTagsResponse response = memoService.updateMemoTags(id, request, userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @DeleteMapping("/memo/{id}")
    public ResponseEntity<Void> deleteMemo(
        @PathVariable("id") String id,
        @AuthenticationPrincipal String userId
    ) {
        memoService.deleteMemo(id, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
