package com.example.memo.acceptance;

import static com.example.memo.memo.service.enums.AiSearchType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.memo.memo.MemoTagService;
import com.example.memo.memo.models.CreateMemoRequest;
import com.example.memo.memo.models.CreateMemoResponse;
import com.example.memo.memo.models.MemoResponse;
import com.example.memo.memo.models.SearchMemoRequest;
import com.example.memo.memo.models.SearchMemoResponse;
import com.example.memo.memo.models.UpdateMemoRequest;
import com.example.memo.memo.models.UpdateMemoResponse;
import com.example.memo.memo.service.MemoService;
import com.example.memo.memo.service.TagService;
import com.example.memo.memo.service.client.AiMemoTagClient;
import com.example.memo.memo.service.client.models.AiCreateResponse;
import com.example.memo.memo.service.client.models.AiCreateResponse.InnerTag;
import com.example.memo.memo.service.client.models.AiSearchResponse;
import com.example.memo.memo.service.enums.AiSearchType;
import com.example.memo.memo.service.models.Memo;
import com.example.memo.memo.service.models.Tag;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class MemoTagServiceTest {

    /*
    @MockBean
    private MemoService memoService;

    @MockBean
    private TagService tagService;

    @MockBean
    private AiMemoTagClient aiMemoTagClient;

    @SpyBean
    private MemoTagService memoTagService;

    private Memo memo;
    private Tag tag;

    @BeforeEach
    void setUp() {
        memo = Memo.builder()
            .id("메모1")
            .content("내일 일정 : 5시 멘토링")
            .tagIds(List.of("태그1"))
            .embedding(List.of(0.1, 0.2, 0.3))
            .build();

        tag = Tag.builder()
            .id("태그1")
            .name("일정")
            .memoIds(List.of("메모1"))
            .embedding(List.of(0.1, 0.2))
            .build();
    }

    @Test
    @DisplayName("모든 메모를 불러온다.")
    void getAllMemos() {
        doReturn(List.of(memo)).when(memoService).getAllMemos();
        doReturn(List.of(tag)).when(tagService).getTagsById(memo.getTagIds());

        List<MemoResponse> responses = memoTagService.getAllMemos();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).content()).isEqualTo("내일 일정 : 5시 멘토링");
        assertThat(responses.get(0).tags()).hasSize(1);
        assertThat(responses.get(0).tags().get(0).name()).isEqualTo("일정");
    }

    @Test
    @DisplayName("메모를 생성한다.")
    void testCreateMemo() {
        CreateMemoRequest request = new CreateMemoRequest("content");
        AiCreateResponse aiResponse = new AiCreateResponse(
            List.of(0.1, 0.2, 0.3),
            List.of("existingTagId"),
            List.of(new InnerTag("메모id", "newTag", List.of(0.1, 0.2), "부모태그"))
        );

        Memo savedMemo = Memo.builder()
            .id("메모1")
            .content("content")
            .tagIds(List.of("existingTagId", "newTagId"))
            .embedding(List.of(0.1, 0.2, 0.3))
            .build();

        Tag existingTag = Tag.builder()
            .id("existingTagId")
            .name("existingTag")
            .memoIds(List.of("메모1"))
            .embedding(List.of(0.1, 0.2))
            .build();

        Tag newTag = Tag.builder()
            .id("newTagId")
            .name("newTag")
            .memoIds(List.of("메모1"))
            .embedding(List.of(0.1, 0.2))
            .build();

        doReturn(aiResponse).when(aiMemoTagClient).createMemo("content");
        doReturn(savedMemo).when(memoService).saveMemo(any(Memo.class));
        doReturn(existingTag).when(tagService).getTagById("existingTagId");
        doReturn(newTag).when(tagService).saveTag(any(Tag.class));

        CreateMemoResponse response = memoTagService.createMemo(request);

        assertThat(response.content()).isEqualTo("content");
        assertThat(response.tags()).hasSize(2);
        assertThat(response.tags().get(1).name()).isEqualTo("newTag");
    }

    @Test
    @DisplayName("메모를 검색한다.")
    void testSearchMemo() {
        AiSearchResponse aiResponse = new AiSearchResponse(
            SIMILARITY,
            "",
            List.of("1"),
            "",
            List.of("tag1")
        );

        SearchMemoRequest request = new SearchMemoRequest("content");

        doReturn(aiResponse).when(aiMemoTagClient).searchMemo("content");
        doReturn(memo).when(memoService).getMemoById("1");
        doReturn(List.of(tag)).when(tagService).getTagsById(List.of("tag1"));

        SearchMemoResponse response = memoTagService.searchMemo(request);

        assertThat(response.memos()).hasSize(1);
        assertThat(response.memos().get(0).tags()).hasSize(1);
        assertThat(response.memos().get(0).tags().get(0).name()).isEqualTo("일정");
    }

    @Test
    @DisplayName("메모를 수정한다.")
    void updateMemo() {
        UpdateMemoRequest request = new UpdateMemoRequest("updated content");
        AiCreateResponse aiResponse = new AiCreateResponse(
            List.of(0.1, 0.2, 0.3),
            List.of(),
            List.of(new InnerTag("메모id", "updatedTag", List.of(0.1, 0.2), "부모태그"))
        );

        Memo updatedMemo = Memo.builder()
            .id("메모1")
            .content("updated content")
            .tagIds(List.of("updatedTagId"))
            .embedding(List.of(0.1, 0.2, 0.3))
            .build();

        Tag updatedTag = Tag.builder()
            .id("updatedTagId")
            .name("updatedTag")
            .memoIds(List.of("메모1"))
            .embedding(List.of(0.1, 0.2))
            .build();

        doReturn(memo).when(memoService).getMemoById("메모1");
        doReturn(aiResponse).when(aiMemoTagClient).createMemo("updated content");
        doReturn(updatedMemo).when(memoService).saveMemo(any(Memo.class));
        doReturn(updatedTag).when(tagService).saveTag(any(Tag.class));

        UpdateMemoResponse response = memoTagService.updateMemo("메모1", request);

        assertThat(response.content()).isEqualTo("updated content");
        assertThat(response.tags()).hasSize(1);
        assertThat(response.tags().get(0).name()).isEqualTo("updatedTag");
    }

    @Test
    @DisplayName("메모를 삭제한다.")
    void testDeleteMemo() {
        doReturn(memo).when(memoService).getMemoById("메모1");
        doReturn(tag).when(tagService).getTagById("태그1");

        memoTagService.deleteMemo("메모1");

        verify(memoService, times(1)).deleteMemo(memo);
        verify(tagService, times(1)).saveTag(tag);
        verify(tagService, times(1)).deleteTag(tag);
    }

     */
}
