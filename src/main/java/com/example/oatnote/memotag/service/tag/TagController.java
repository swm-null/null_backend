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
    @PostMapping("/tag/child")
    public ResponseEntity<CreateChildTagResponse> createChildTag(
        @RequestParam(value = "id", required = false) String id,
        @RequestBody @Valid CreateChildTagRequest request,
        @AuthenticationPrincipal String userId
    ) {
        CreateChildTagResponse response = memoService.createChildTag(id, request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @GetMapping("/tag/children")
    public ResponseEntity<List<TagResponse>> getChildTags(
        @RequestParam(value = "id", required = false) String id,
        @AuthenticationPrincipal String userId
    ) {
        List<TagResponse> response = memoService.getChildTags(id, userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @GetMapping("/tag/ancestors")
    public ResponseEntity<List<TagResponse>> getAncestorTags(
        @RequestParam(value = "id") String id,
        @AuthenticationPrincipal String userId
    ) {
        List<TagResponse> response = memoService.getAncestorTags(id, userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @GetMapping("/tag/descendants")
    public ResponseEntity<TagsResponse> getTags(
        @RequestParam(value = "id", required = false) String id,
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "limit", defaultValue = "10") Integer limit,
        @AuthenticationPrincipal String userId
    ) {
        TagsResponse response = memoService.getTags(id, page, limit, userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @PutMapping("/tag/{id}")
    public ResponseEntity<UpdateTagResponse> updateTag(
        @PathVariable("id") String id,
        @RequestBody @Valid UpdateTagRequest request,
        @AuthenticationPrincipal String userId
    ) {
        UpdateTagResponse response = memoService.updateTag(id, request, userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @DeleteMapping("/tag/{id}")
    public ResponseEntity<Void> deleteTag(
        @PathVariable("id") String id,
        @AuthenticationPrincipal String userId
    ) {
        memoService.deleteTag(id, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
