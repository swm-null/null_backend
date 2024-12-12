package com.example.oatnote.memotag.service.tag;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.oatnote.memotag.dto.CreateChildTagRequest;
import com.example.oatnote.memotag.dto.CreateChildTagResponse;
import com.example.oatnote.memotag.dto.TagsResponse;
import com.example.oatnote.memotag.dto.UpdateTagRequest;
import com.example.oatnote.memotag.dto.UpdateTagResponse;
import com.example.oatnote.memotag.dto.innerDto.TagResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Tag", description = "태그 API")
public interface TagApiDoc {

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true))),
        }
    )
    @Operation(summary = "특정 태그 하위에 자식 태그 생성")
    @PostMapping("/tag/child")
    ResponseEntity<CreateChildTagResponse> createChildTag(
        @RequestParam(value = "tagId", required = false) String tagId,
        @RequestBody @Valid CreateChildTagRequest request,
        @AuthenticationPrincipal String userId
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true))),
        }
    )
    @Operation(summary = "특정 태그의 전체 자식 태그 리스트 조회")
    @GetMapping("/tags/children")
    ResponseEntity<List<TagResponse>> getChildTags(
        @RequestParam(value = "tagId", required = false) String tagId,
        @AuthenticationPrincipal String userId
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true))),
        }
    )
    @Operation(summary = "특정 태그의 조상 태그 리스트 조회")
    @GetMapping("/tags/ancestors")
    ResponseEntity<List<TagResponse>> getAncestorTags(
        @RequestParam(value = "tagId") String tagId,
        @AuthenticationPrincipal String userId
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true))),
        }
    )
    @Operation(summary = "특정 태그와 그 자식, 손자 태그 리스트 조회")
    @GetMapping("/tags/descendants")
    ResponseEntity<TagsResponse> getTags(
        @RequestParam(value = "tagId", required = false) String tagId,
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "limit", defaultValue = "10") Integer limit,
        @AuthenticationPrincipal String userId
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true))),
        }
    )
    @Operation(summary = "태그 수정")
    @PutMapping("/tag/{tagId}")
    ResponseEntity<UpdateTagResponse> updateTag(
        @PathVariable("tagId") String tagId,
        @RequestBody @Valid UpdateTagRequest request,
        @AuthenticationPrincipal String userId
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true))),
        }
    )
    @Operation(summary = "태그 삭제")
    @DeleteMapping("/tag/{tagId}")
    ResponseEntity<Void> deleteTag(
        @PathVariable("tagId") String tagId,
        @AuthenticationPrincipal String userId
    );
}
