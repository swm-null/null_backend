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

import com.example.oatnote.domain.memotag.dto.CreateChildTagRequest;
import com.example.oatnote.domain.memotag.dto.CreateChildTagResponse;
import com.example.oatnote.domain.memotag.dto.CreateMemoRequest;
import com.example.oatnote.domain.memotag.dto.CreateMemoResponse;
import com.example.oatnote.domain.memotag.dto.CreateMemosRequest;
import com.example.oatnote.domain.memotag.dto.CreateSearchHistoryRequest;
import com.example.oatnote.domain.memotag.dto.CreateSearchHistoryResponse;
import com.example.oatnote.domain.memotag.dto.MemosResponse;
import com.example.oatnote.domain.memotag.dto.SearchHistoriesResponse;
import com.example.oatnote.domain.memotag.dto.SearchMemosUsingAiResponse;
import com.example.oatnote.domain.memotag.dto.SearchMemosUsingDbResponse;
import com.example.oatnote.domain.memotag.dto.TagsResponse;
import com.example.oatnote.domain.memotag.dto.UpdateMemoRequest;
import com.example.oatnote.domain.memotag.dto.UpdateMemoResponse;
import com.example.oatnote.domain.memotag.dto.UpdateMemoTagsRequest;
import com.example.oatnote.domain.memotag.dto.UpdateMemoTagsResponse;
import com.example.oatnote.domain.memotag.dto.UpdateTagRequest;
import com.example.oatnote.domain.memotag.dto.UpdateTagResponse;
import com.example.oatnote.domain.memotag.dto.enums.MemoSortOrderTypeEnum;
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
        CreateMemoResponse createMemoResponse = memoTagService.createMemo(createMemoRequest, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createMemoResponse);
    }

    @PostMapping("/linkedMemo")
    public ResponseEntity<CreateMemoResponse> createLinkedMemo(
        @RequestParam(value = "tagId", required = false) String tagId,
        @RequestBody @Valid CreateMemoRequest createMemoRequest,
        @AuthenticationPrincipal String userId
    ) {
        CreateMemoResponse createMemoResponse = memoTagService.createLinkedMemo(tagId, createMemoRequest, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createMemoResponse);
    }

    @PostMapping("/memos")
    public ResponseEntity<Void> createMemos(
        @RequestBody @Valid CreateMemosRequest createMemosRequest,
        @AuthenticationPrincipal String userId
    ) {
        memoTagService.createMemos(createMemosRequest, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/childTag")
    public ResponseEntity<CreateChildTagResponse> createChildTag(
        @RequestParam(value = "tagId", required = false) String tagId,
        @RequestBody @Valid CreateChildTagRequest createChildTagRequest,
        @AuthenticationPrincipal String userId
    ) {
        CreateChildTagResponse createChildTagResponse = memoTagService.createChildTag(tagId, createChildTagRequest, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createChildTagResponse);
    }

    @PostMapping("/memos/search/history")
    public ResponseEntity<CreateSearchHistoryResponse> createSearchHistory(
        @RequestBody @Valid CreateSearchHistoryRequest createSearchHistoryRequest,
        @AuthenticationPrincipal String userId
    ) {
        CreateSearchHistoryResponse createSearchHistoryResponse = memoTagService.createSearchHistory(
            createSearchHistoryRequest,
            userId
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(createSearchHistoryResponse);
    }

    @GetMapping("/ancestorTags")
    public ResponseEntity<List<TagResponse>> getAncestorTags(
        @RequestParam(value = "tagId") String tagId,
        @AuthenticationPrincipal String userId
    ) {
        List<TagResponse> childTagsResponse = memoTagService.getAncestorTags(tagId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(childTagsResponse);
    }

    @GetMapping("/childTags")
    public ResponseEntity<List<TagResponse>> getChildTags(
        @RequestParam(value = "tagId", required = false) String tagId,
        @AuthenticationPrincipal String userId
    ) {
        List<TagResponse> childTagsResponse = memoTagService.getChildTags(tagId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(childTagsResponse);
    }

    @GetMapping("/tags")
    public ResponseEntity<TagsResponse> getTags(
        @RequestParam(value = "tagId", required = false) String tagId,
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "limit", defaultValue = "10") Integer limit,
        @AuthenticationPrincipal String userId
    ) {
        TagsResponse tagsResponse = memoTagService.getTags(tagId, page, limit, userId);
        return ResponseEntity.status(HttpStatus.OK).body(tagsResponse);
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
        MemosResponse memosResponse = memoTagService.getMemos(tagId, page, limit, sortOrder, isLinked, userId);
        return ResponseEntity.status(HttpStatus.OK).body(memosResponse);
    }

    @GetMapping("/memos/search/ai")
    public ResponseEntity<SearchMemosUsingAiResponse> searchMemosUsingAi(
        @RequestParam(value = "searchHistoryId") String searchHistoryId,
        @AuthenticationPrincipal String userId
    ) {
        SearchMemosUsingAiResponse searchMemosUsingAiResponse = memoTagService.searchMemosUsingAi(
            searchHistoryId,
            userId
        );
        return ResponseEntity.status(HttpStatus.OK).body(searchMemosUsingAiResponse);
    }

    @GetMapping("/memos/search/db")
    public ResponseEntity<SearchMemosUsingDbResponse> searchMemosUsingDb(
        @RequestParam(value = "searchHistoryId") String searchHistoryId,
        @AuthenticationPrincipal String userId
    ) {
        SearchMemosUsingDbResponse searchMemosUsingDbResponse = memoTagService.searchMemosUsingDb(
            searchHistoryId,
            userId
        );
        return ResponseEntity.status(HttpStatus.OK).body(searchMemosUsingDbResponse);
    }

    @GetMapping("/memos/search/histories")
    public ResponseEntity<SearchHistoriesResponse> getSearchHistories(
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "limit", defaultValue = "15") Integer limit,
        @AuthenticationPrincipal String userId
    ) {
        SearchHistoriesResponse searchHistoriesResponse = memoTagService.getSearchHistories(page, limit, userId);
        return ResponseEntity.status(HttpStatus.OK).body(searchHistoriesResponse);
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

    @PutMapping("/memo/{memoId}/tags")
    public ResponseEntity<UpdateMemoTagsResponse> updateMemoTags(
        @PathVariable("memoId") String memoId,
        @RequestBody @Valid UpdateMemoTagsRequest updateMemoTagsRequest,
        @AuthenticationPrincipal String userId
    ) {
        UpdateMemoTagsResponse updateMemoTagsResponse = memoTagService.updateMemoTags(
            memoId,
            updateMemoTagsRequest,
            userId
        );
        return ResponseEntity.status(HttpStatus.OK).body(updateMemoTagsResponse);
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
