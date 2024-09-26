package com.example.oatnote.memotag;

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

import com.example.oatnote.memotag.dto.ChildTagsWithMemosResponse;
import com.example.oatnote.memotag.dto.CreateMemoRequest;
import com.example.oatnote.memotag.dto.CreateMemoResponse;
import com.example.oatnote.memotag.dto.CreateMemosRequest;
import com.example.oatnote.memotag.dto.PagedMemosResponse;
import com.example.oatnote.memotag.dto.SearchMemoRequest;
import com.example.oatnote.memotag.dto.SearchMemoResponse;
import com.example.oatnote.memotag.dto.UpdateMemoRequest;
import com.example.oatnote.memotag.dto.UpdateMemoResponse;
import com.example.oatnote.memotag.dto.UpdateTagRequest;
import com.example.oatnote.memotag.dto.UpdateTagResponse;
import com.example.oatnote.memotag.dto.enums.SortOrderTypeEnum;
import com.example.oatnote.memotag.dto.innerDto.TagResponse;

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
    public ResponseEntity<PagedMemosResponse> getMemosByTag(
        @RequestParam(value = "tagId", required = false) String tagId,
        @RequestParam(name = "memoPage", defaultValue = "1") Integer memoPage,
        @RequestParam(name = "memoLimit", defaultValue = "10") Integer memoLimit,
        @RequestParam(name = "sortOrder") SortOrderTypeEnum sortOrder,
        @AuthenticationPrincipal String userId
    ) {
        PagedMemosResponse pagedMemosResponse = memoTagService.getMemos(
            tagId,
            memoPage,
            memoLimit,
            sortOrder,
            userId
        );
        return ResponseEntity.status(HttpStatus.OK).body(pagedMemosResponse);
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

    @PostMapping("/memos/search")
    public ResponseEntity<SearchMemoResponse> searchMemos(
        @RequestBody @Valid SearchMemoRequest searchMemoRequest,
        @AuthenticationPrincipal String userId
    ) {
        SearchMemoResponse searchMemoResponse = memoTagService.searchMemos(searchMemoRequest, userId);
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
