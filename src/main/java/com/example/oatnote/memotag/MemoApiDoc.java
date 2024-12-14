package com.example.oatnote.memotag;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Memo", description = "메모 API")
public interface MemoApiDoc {

    @ApiResponses({
        @ApiResponse(responseCode = "201"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(hidden = true))),
    })
    @Operation(summary = "메모 생성")
    @PostMapping("/memo")
    ResponseEntity<CreateMemoResponse> createMemo(
        @RequestBody @Valid CreateMemoRequest request,
        @AuthenticationPrincipal String userId
    );

    @ApiResponses({
        @ApiResponse(responseCode = "201"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(hidden = true))),
    })
    @Operation(summary = "특정 태그와 연결된 메모 생성")
    @PostMapping("/memo/tag")
    ResponseEntity<CreateMemoResponse> createMemoLinkedTag(
        @RequestParam(value = "tagId", required = false) String tagId,
        @RequestBody @Valid CreateMemoRequest request,
        @AuthenticationPrincipal String userId
    );

    @ApiResponses({
        @ApiResponse(responseCode = "201"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(hidden = true))),
    })
    @Operation(summary = "파일로 메모 리스트 생성")
    @PostMapping("/memos/file")
    ResponseEntity<Void> createMemosByFile(
        @RequestBody @Valid CreateMemosByFileRequest request,
        @AuthenticationPrincipal String userId
    );

    @ApiResponses({
        @ApiResponse(responseCode = "201"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(hidden = true))),
    })
    @Operation(summary = "메모 검색 기록 생성")
    @PostMapping("/memos/search/history")
    ResponseEntity<CreateSearchHistoryResponse> createSearchHistory(
        @RequestBody @Valid CreateSearchHistoryRequest request,
        @AuthenticationPrincipal String userId
    );

    @ApiResponses({
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true))),
    })
    @Operation(summary = "특정 태그의 메모 리스트 조회")
    @GetMapping("/memos")
    ResponseEntity<MemosResponse> getMemosLinkedTag(
        @RequestParam(value = "tagId", required = false) String tagId,
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "limit", defaultValue = "10") Integer limit,
        @RequestParam(value = "sortOrder") MemoSortOrderTypeEnum sortOrder,
        @RequestParam(value = "isLinked", required = false) Boolean isLinked,
        @AuthenticationPrincipal String userId
    );

    @ApiResponses({
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(hidden = true))),
    })
    @Operation(summary = "AI 기반 메모 검색")
    @GetMapping("/memos/search/ai")
    ResponseEntity<SearchMemosUsingAiResponse> searchMemosUsingAi(
        @RequestParam(value = "searchHistoryId") String searchHistoryId,
        @AuthenticationPrincipal String userId
    );

    @ApiResponses({
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(hidden = true))),
    })
    @Operation(summary = "DB 기반 메모 검색")
    @GetMapping("/memos/search/db")
    ResponseEntity<SearchMemosUsingDbResponse> searchMemosUsingDb(
        @RequestParam(value = "searchHistoryId") String searchHistoryId,
        @AuthenticationPrincipal String userId
    );

    @ApiResponses({
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true))),
    })
    @Operation(summary = "메모 검색 기록 조회")
    @GetMapping("/memos/search/histories")
    ResponseEntity<SearchHistoriesResponse> getSearchHistories(
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "limit", defaultValue = "15") Integer limit,
        @AuthenticationPrincipal String userId
    );

    @ApiResponses({
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(hidden = true))),
    })
    @Operation(summary = "메모 수정")
    @PutMapping("/memo/{id}")
    ResponseEntity<UpdateMemoResponse> updateMemo(
        @PathVariable("id") String id,
        @RequestBody @Valid UpdateMemoRequest request,
        @AuthenticationPrincipal String userId
    );

    @ApiResponses({
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(hidden = true))),
    })
    @Operation(summary = "메모 내용 업데이트 및 태그 재생성")
    @PutMapping("/memo/{id}/tags")
    ResponseEntity<UpdateMemoTagsResponse> updateMemoTags(
        @PathVariable("id") String id,
        @RequestBody @Valid UpdateMemoTagsRequest request,
        @AuthenticationPrincipal String userId
    );

    @ApiResponses({
        @ApiResponse(responseCode = "204"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true))),
    })
    @Operation(summary = "메모 삭제")
    @DeleteMapping("/memo/{id}")
    ResponseEntity<Void> deleteMemo(
        @PathVariable("id") String id,
        @AuthenticationPrincipal String userId
    );
}
