package com.example.oatnote.domain.memotag;

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

import com.example.oatnote.domain.memotag.dto.ChildTagsWithMemosResponse;
import com.example.oatnote.domain.memotag.dto.CreateMemoRequest;
import com.example.oatnote.domain.memotag.dto.CreateMemoResponse;
import com.example.oatnote.domain.memotag.dto.CreateMemosRequest;
import com.example.oatnote.domain.memotag.dto.MemosResponse;
import com.example.oatnote.domain.memotag.dto.SearchHistoriesResponse;
import com.example.oatnote.domain.memotag.dto.SearchMemosRequest;
import com.example.oatnote.domain.memotag.dto.SearchMemosResponse;
import com.example.oatnote.domain.memotag.dto.UpdateMemoRequest;
import com.example.oatnote.domain.memotag.dto.UpdateMemoResponse;
import com.example.oatnote.domain.memotag.dto.UpdateTagRequest;
import com.example.oatnote.domain.memotag.dto.UpdateTagResponse;
import com.example.oatnote.domain.memotag.dto.enums.SortOrderTypeEnum;
import com.example.oatnote.domain.memotag.dto.innerDto.TagResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemoTagController implements MemoTagApiDoc {

    private final MemoTagService memoTagService;

    @PostMapping("/memo")
    public ResponseEntity<CreateMemoResponse> createMemo(
        @RequestBody @Valid CreateMemoRequest createMemoRequest,
        @AuthenticationPrincipal String userId
    ) {
        CreateMemoResponse createMemoResponse = memoTagService.createMemoTags(createMemoRequest, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createMemoResponse);
    }

    @PostMapping("/memos/email")
    public ResponseEntity<Void> createMemosByEmail(
        @RequestBody @Valid CreateMemosRequest createMemosRequest
    ) {
        memoTagService.createMemosTags(createMemosRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/tag/memos")
    public ResponseEntity<MemosResponse> getMemosByTag(
        @RequestParam(value = "tagId", required = false) String tagId,
        @RequestParam(name = "memoPage", defaultValue = "1") Integer memoPage,
        @RequestParam(name = "memoLimit", defaultValue = "10") Integer memoLimit,
        @RequestParam(name = "sortOrder") SortOrderTypeEnum sortOrder,
        @AuthenticationPrincipal String userId
    ) {
        MemosResponse memosResponse = memoTagService.getMemos(
            tagId,
            memoPage,
            memoLimit,
            sortOrder,
            userId
        );
        return ResponseEntity.status(HttpStatus.OK).body(memosResponse);
    }

    @GetMapping("/childTags")
    public ResponseEntity<List<TagResponse>> getChildTags(
        @RequestParam(value = "parentTagId", required = false) String parentTagId,
        @AuthenticationPrincipal String userId
    ) {
        List<TagResponse> childTags = memoTagService.getChildTags(parentTagId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(childTags);
    }

    @GetMapping("/childTags/memos")
    public ResponseEntity<ChildTagsWithMemosResponse> getChildTagsWithMemos(
        @RequestParam(value = "parentTagId", required = false) String parentTagId,
        @RequestParam(name = "tagPage", defaultValue = "1") Integer tagPage,
        @RequestParam(name = "tagLimit", defaultValue = "10") Integer tagLimit,
        @RequestParam(name = "memoPage", defaultValue = "1") Integer memoPage,
        @RequestParam(name = "memoLimit", defaultValue = "10") Integer memoLimit,
        @RequestParam(name = "sortOrder") SortOrderTypeEnum sortOrder,
        @AuthenticationPrincipal String userId
    ) {
        ChildTagsWithMemosResponse childTagsWithMemosResponse = memoTagService.getChildTagsWithMemos(
            parentTagId,
            tagPage,
            tagLimit,
            memoPage,
            memoLimit,
            sortOrder,
            userId
        );
        return ResponseEntity.status(HttpStatus.OK).body(childTagsWithMemosResponse);
    }

    @GetMapping("/memos/search/histories")
    public ResponseEntity<SearchHistoriesResponse> getSearchHistories(
        @RequestParam(name = "query", defaultValue = "") String query,
        @RequestParam(name = "searchHistoryPage", defaultValue = "1") Integer searchHistoryPage,
        @RequestParam(name = "searchHistoryLimit", defaultValue = "15") Integer searchHistoryLimit,
        @AuthenticationPrincipal String userId
    ) {
        SearchHistoriesResponse searchHistoriesResponse = memoTagService.getSearchHistories(
            query,
            searchHistoryPage,
            searchHistoryLimit,
            userId
        );
        return ResponseEntity.status(HttpStatus.OK).body(searchHistoriesResponse);
    }

    @PostMapping("/memos/search")
    public ResponseEntity<SearchMemosResponse> searchMemos(
        @RequestBody @Valid SearchMemosRequest searchMemosRequest,
        @AuthenticationPrincipal String userId
    ) {
        SearchMemosResponse searchMemosResponse = memoTagService.searchMemos(searchMemosRequest, userId);
        return ResponseEntity.status(HttpStatus.OK).body(searchMemosResponse);
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
