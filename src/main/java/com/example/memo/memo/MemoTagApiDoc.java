package com.example.memo.memo;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.memo.memo.models.CreateMemoRequest;
import com.example.memo.memo.models.CreateMemoResponse;
import com.example.memo.memo.models.MemoResponse;
import com.example.memo.memo.models.SearchMemoRequest;
import com.example.memo.memo.models.SearchMemoResponse;
import com.example.memo.memo.models.TagResponse;
import com.example.memo.memo.models.UpdateMemoRequest;
import com.example.memo.memo.models.UpdateMemoResponse;

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
            @ApiResponse(responseCode = "200"),
        }
    )
    @Operation(summary = "메모 전체 조회")
    @GetMapping("/memos")
    ResponseEntity<List<MemoResponse>> getAllMemos();

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(hidden = true))),
        }
    )
    @Operation(summary = "특정 태그의 메모 검색")
    @GetMapping("/memos/tags/{tagId}")
    ResponseEntity<List<MemoResponse>> getMemosByTagId(
        @PathVariable String tagId
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(hidden = true))),
        }
    )
    @Operation(summary = "메모 생성")
    @PostMapping("/memos")
    ResponseEntity<CreateMemoResponse> createMemo(
        @RequestBody @Valid CreateMemoRequest createMemoRequest
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(hidden = true))),
        }
    )
    @Operation(summary = "메모 검색")
    @PostMapping("/memos/search")
    ResponseEntity<SearchMemoResponse> searchMemos(
        @RequestBody @Valid SearchMemoRequest searchMemoRequest
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(hidden = true))),
        }
    )
    @Operation(summary = "메모 수정")
    @PutMapping("/memos/{memoId}")
    ResponseEntity<UpdateMemoResponse> updateMemo(
        @PathVariable("memoId") String memoId,
        @RequestBody @Valid UpdateMemoRequest updateMemoRequest
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(hidden = true))),
        }
    )
    @Operation(summary = "메모 삭제")
    @DeleteMapping("/memos/{memoId}")
    ResponseEntity<Void> deleteMemo(
        @PathVariable("memoId") String memoId
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
        }
    )
    @Operation(summary = "태그 전체 조회")
    @GetMapping("/tags")
    ResponseEntity<List<TagResponse>> getAllTags();

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true))),
        }
    )
    @Operation(summary = "특정 계층의 태그 조회")
    @GetMapping("/tags/depth/{depth}")
    ResponseEntity<List<TagResponse>> getTagsByDepth(
        @PathVariable("depth") int depth
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true))),
        }
    )
    @Operation(summary = "특정 태그의 자식 태그 조회")
    @GetMapping("/tags/{tagId}/childTags")
    ResponseEntity<List<TagResponse>> getChildTagsByTagId(
        @PathVariable("tagId") String tagId
    );
}
