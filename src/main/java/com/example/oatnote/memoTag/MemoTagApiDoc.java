package com.example.oatnote.memoTag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Memo", description = "메모 관리 API")
public interface MemoTagApiDoc {

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(hidden = true))),
        }
    )
    @Operation(summary = "메모 생성")
    @PostMapping("/memo/tags")
    ResponseEntity<CreateMemoTagsResponse> createMemoTags(
        @RequestBody @Valid CreateMemoTagsRequest createMemoTagsRequest
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(hidden = true))),
        }
    )
    @Operation(summary = "이메일을 통한 메모 태그 생성")
    @PostMapping("/memos/tags/email")
    ResponseEntity<Void> createMemosTagsByEmail(
        @RequestBody @Valid CreateMemosTagsRequest createMemosTagsRequest
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
        }
    )
    @Operation(summary = "자식 메모 태그 조회")
    @GetMapping("/memos/tags/{parentTagId}")
    ResponseEntity<ChildMemosTagsResponse> getChildMemosTags(
        @PathVariable("parentTagId") String parentTagId,
        @RequestParam(name = "tagPage", defaultValue = "1") Integer tagPage,
        @RequestParam(name = "tagLimit", defaultValue = "10", required = false) Integer tagLimit,
        @RequestParam(name = "memoPage", defaultValue = "1") Integer memoPage,
        @RequestParam(name = "memoLimit", defaultValue = "10", required = false) Integer memoLimit
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
        }
    )
    @Operation(summary = "특정 태그의 메모 조회")
    @GetMapping("/memos/tag/{tagId}")
    ResponseEntity<PagedMemosTagsResponse> getMemosByTagId(
        @PathVariable("tagId") String tagId,
        @RequestParam(name = "memoPage", defaultValue = "1") Integer memoPage,
        @RequestParam(name = "memoLimit", defaultValue = "10", required = false) Integer memoLimit
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(hidden = true))),
        }
    )
    @Operation(summary = "AI 검색을 통한 메모 태그 조회")
    @PostMapping("/memos/tags/search")
    ResponseEntity<SearchMemoResponse> getMemosTagsByAISearch(
        @RequestBody @Valid SearchMemoRequest searchMemoRequest
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(hidden = true))),
        }
    )
    @Operation(summary = "메모 수정")
    @PutMapping("/memo/{memoId}")
    ResponseEntity<UpdateMemoResponse> updateMemo(
        @PathVariable("memoId") String memoId,
        @RequestBody @Valid UpdateMemoRequest updateMemoRequest
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true))),
        }
    )
    @Operation(summary = "태그 수정")
    @PutMapping("/tag/{tagId}")
    ResponseEntity<UpdateTagResponse> updateTag(
        @PathVariable("tagId") String tagId,
        @RequestBody @Valid UpdateTagRequest updateTagRequest
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true))),
        }
    )
    @Operation(summary = "메모 삭제")
    @DeleteMapping("/memo/{memoId}")
    ResponseEntity<Void> deleteMemo(
        @PathVariable("memoId") String memoId
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true))),
        }
    )
    @Operation(summary = "태그 삭제")
    @DeleteMapping("/tag/{tagId}")
    ResponseEntity<Void> deleteTag(
        @PathVariable("tagId") String tagId
    );
}
