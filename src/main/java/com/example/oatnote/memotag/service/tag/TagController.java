package com.example.oatnote.memotag.service.tag;

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

import com.example.oatnote.memotag.dto.CreateChildTagRequest;
import com.example.oatnote.memotag.dto.CreateChildTagResponse;
import com.example.oatnote.memotag.dto.TagsResponse;
import com.example.oatnote.memotag.dto.UpdateTagRequest;
import com.example.oatnote.memotag.dto.UpdateTagResponse;
import com.example.oatnote.memotag.dto.innerDto.TagResponse;
import com.example.oatnote.memotag.service.MemoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TagController implements TagApiDoc {

    private final MemoService memoService;

    @Override
    @PostMapping("/childTag")
    public ResponseEntity<CreateChildTagResponse> createChildTag(
        @RequestParam(value = "tagId", required = false) String tagId,
        @RequestBody @Valid CreateChildTagRequest request,
        @AuthenticationPrincipal String userId
    ) {
        CreateChildTagResponse response = memoService.createChildTag(tagId, request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @GetMapping("/ancestorTags")
    public ResponseEntity<List<TagResponse>> getAncestorTags(
        @RequestParam(value = "tagId") String tagId,
        @AuthenticationPrincipal String userId
    ) {
        List<TagResponse> response = memoService.getAncestorTags(tagId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @GetMapping("/childTags")
    public ResponseEntity<List<TagResponse>> getChildTags(
        @RequestParam(value = "tagId", required = false) String tagId,
        @AuthenticationPrincipal String userId
    ) {
        List<TagResponse> response = memoService.getChildTags(tagId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @GetMapping("/tags")
    public ResponseEntity<TagsResponse> getTags(
        @RequestParam(value = "tagId", required = false) String tagId,
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "limit", defaultValue = "10") Integer limit,
        @AuthenticationPrincipal String userId
    ) {
        TagsResponse response = memoService.getTags(tagId, page, limit, userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @PutMapping("/tag/{tagId}")
    public ResponseEntity<UpdateTagResponse> updateTag(
        @PathVariable("tagId") String tagId,
        @RequestBody @Valid UpdateTagRequest request,
        @AuthenticationPrincipal String userId
    ) {
        UpdateTagResponse response = memoService.updateTag(tagId, request, userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @DeleteMapping("/tag/{tagId}")
    public ResponseEntity<Void> deleteTag(
        @PathVariable("tagId") String tagId,
        @AuthenticationPrincipal String userId
    ) {
        memoService.deleteTag(tagId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
