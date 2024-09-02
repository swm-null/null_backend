package com.example.oatnote.memo;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.oatnote.memo.models.CreateMemosTagsRequest;
import com.example.oatnote.memo.models.CreateMemoTagsRequest;
import com.example.oatnote.memo.models.CreateMemoTagsResponse;
import com.example.oatnote.memo.models.CreateTagRequest;
import com.example.oatnote.memo.models.CreateTagResponse;
import com.example.oatnote.memo.models.InnerResponse.MemoResponse;
import com.example.oatnote.memo.models.SearchMemoRequest;
import com.example.oatnote.memo.models.SearchMemoResponse;
import com.example.oatnote.memo.models.InnerResponse.TagResponse;
import com.example.oatnote.memo.models.UpdateMemoRequest;
import com.example.oatnote.memo.models.UpdateMemoResponse;
import com.example.oatnote.memo.models.UpdateTagRequest;
import com.example.oatnote.memo.models.UpdateTagResponse;

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
    @PostMapping("/memos")
    ResponseEntity<CreateMemoTagsResponse> createMemo(
        @RequestBody @Valid CreateMemoTagsRequest createMemoTagsRequest
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(hidden = true))),
        }
    )
    @Operation(summary = "카카오톡으로 메모 리스트 생성")
    @PostMapping("/memos/kakao")
    ResponseEntity<List<CreateMemoTagsResponse>> createKakaoMemos(
        @RequestBody @Valid CreateMemosTagsRequest createMemosTagsRequest
    );

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
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(hidden = true))),
        }
    )
    @Operation(summary = "AI 메모 검색")
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
        }
    )
    @Operation(summary = "메모 삭제")
    @DeleteMapping("/memos/{memoId}")
    ResponseEntity<Void> deleteMemo(
        @PathVariable("memoId") String memoId
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(hidden = true))),
        }
    )
    @Operation(summary = "특정 메모에 태그 생성")
    @PostMapping("/memos/{memoId}/tags")
    ResponseEntity<CreateTagResponse> createTag(
        @PathVariable("memoId") String memoId,
        @RequestBody @Valid CreateTagRequest createTagRequest
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
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(hidden = true))),
        }
    )
    @Operation(summary = "루트 태그 조회")
    @GetMapping("/tags/root")
    ResponseEntity<List<TagResponse>> getRootTags();

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true))),
        }
    )
    @Operation(summary = "특정 태그의 자식 태그 조회")
    @GetMapping("/tags/{tagId}/childTags")
    ResponseEntity<List<TagResponse>> getChildTags(
        @PathVariable("tagId") String tagId
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(hidden = true))),
        }
    )
    @Operation(summary = "태그 수정")
    @PutMapping("/tags/{tagId}")
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
    @Operation(summary = "태그 삭제")
    @DeleteMapping("/tags/{tagId}")
    ResponseEntity<Void> deleteTag(
        @PathVariable("tagId") String tagId
    );
}
