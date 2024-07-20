package com.example.memo.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.memo.memo.models.CreateMemoRequest;
import com.example.memo.memo.models.CreateMemoResponse;
import com.example.memo.memo.models.MemoResponse;
import com.example.memo.memo.models.SearchMemoRequest;
import com.example.memo.memo.models.SearchMemoResponse;
import com.example.memo.memo.models.UpdateMemoRequest;
import com.example.memo.memo.models.UpdateMemoResponse;
import com.example.memo.memo.service.client.AiMemoClient;
import com.example.memo.memo.service.MemoRepository;
import com.example.memo.memo.MemoTagService;
import com.example.memo.memo.service.TagRepository;
import com.example.memo.memo.service.client.models.AiCreateResponse;
import com.example.memo.memo.service.client.models.AiCreateResponse.InnerTag;
import com.example.memo.memo.service.client.models.AiSearchResponse;
import com.example.memo.memo.service.models.Memo;
import com.example.memo.memo.service.models.Tag;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MemoTagServiceTest { //TODO

    @Mock
    private MemoRepository memoRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private AiMemoClient aiMemoClient;

    @SpyBean
    private MemoTagService memoTagService;

    private Memo memo;
    private Tag tag;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        memo = Memo.builder()
            .id("메모1")
            .content("내일 일정 : 5시 멘토링")
            .tags(List.of("태그1"))
            .embedding(List.of(0.1, 0.2, 0.3))
            .build();

        tag = Tag.builder()
            .id("태그1")
            .name("일정")
            .memos(List.of("메모1"))
            .embedding(List.of(0.1, 0.2))
            .build();
    }

    @Test
    @DisplayName("모든 메모를 불러온다.")
    void getAllMemos() {
        when(memoRepository.findAll()).thenReturn(List.of(memo));
        when(tagRepository.findAllById(memo.getTags())).thenReturn(List.of(tag));

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
            List.of(0.1, 0.2),
            List.of("existingTagId"),
            List.of(new InnerTag("newTag", List.of(0.1, 0.2)))
        );
        Tag existingTag = Tag.builder()
            .id("existingTagId")
            .name("existingTag")
            .memos(List.of("1"))
            .parent("parent")
            .child(List.of())
            .embedding(List.of(0.1, 0.2))
            .build();
        Tag newTag = Tag.builder()
            .id("newTagId")
            .name("newTag")
            .memos(List.of("1"))
            .parent("parent")
            .child(List.of())
            .embedding(List.of(0.1, 0.2))
            .build();

        when(aiMemoClient.createMemo("content")).thenReturn(aiResponse);
        when(memoRepository.save(any(Memo.class))).thenReturn(memo);
        when(tagRepository.findById("existingTagId")).thenReturn(Optional.of(existingTag));
        when(tagRepository.save(any(Tag.class))).thenReturn(newTag);

        CreateMemoResponse response = memoTagService.createMemo(request);

        assertThat(response.content()).isEqualTo("content");
        assertThat(response.tags()).hasSize(2);
        assertThat(response.tags().get(1).name()).isEqualTo("newTag");
    }

    @Test
    @DisplayName("메모를 검색한다.")
    void testSearchMemo() {
        AiSearchResponse aiResponse = new AiSearchResponse(
            "similarity",
            "",
            List.of("1"), // IDs
            "",
            List.of("tag1") // Tags
        );

        SearchMemoRequest request = new SearchMemoRequest("content");

        when(aiMemoClient.searchMemo("content")).thenReturn(aiResponse);
        when(memoRepository.findById("1")).thenReturn(Optional.of(memo));
        when(tagRepository.findAllById(List.of("tag1"))).thenReturn(List.of(tag));

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
            List.of(0.1, 0.2),
            List.of(),
            List.of(new InnerTag("updatedTag", List.of(0.1, 0.2)))
        );
        Memo updatedMemo = Memo.builder()
            .id("1")
            .content("updated content")
            .tags(List.of("updatedTagId"))
            .embedding(List.of(0.1, 0.2))
            .build();
        Tag updatedTag = Tag.builder()
            .id("updatedTagId")
            .name("updatedTag")
            .memos(List.of("1"))
            .parent("parent")
            .child(List.of())
            .embedding(List.of(0.1, 0.2))
            .build();

        when(memoRepository.findById("1")).thenReturn(Optional.of(memo));
        when(aiMemoClient.createMemo("updated content")).thenReturn(aiResponse);
        when(memoRepository.save(any(Memo.class))).thenReturn(updatedMemo);
        when(tagRepository.save(any(Tag.class))).thenReturn(updatedTag);

        UpdateMemoResponse response = memoTagService.updateMemo("1", request);

        assertThat(response.content()).isEqualTo("updated content");
        assertThat(response.tags()).hasSize(1);
        assertThat(response.tags().get(0).name()).isEqualTo("updatedTag");
    }

    @Test
    @DisplayName("메모를 삭제한다.")
    void testDeleteMemo() {
        doReturn(memo).when(memoRepository).findById(eq("메모1"));

        memoTagService.deleteMemo("메모1");

        verify(memoRepository, times(1)).delete(memo);
        verify(tagRepository, times(1)).delete(tag);
    }
}
