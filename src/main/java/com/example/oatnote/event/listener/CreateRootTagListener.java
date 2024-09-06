package com.example.oatnote.event.listener;

import java.util.ArrayList;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.oatnote.memoTag.service.tag.TagService;
import com.example.oatnote.memoTag.service.tag.model.Tag;
import com.example.oatnote.event.model.UserRegisteredEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateRootTagListener {

    private final TagService tagService;

    @EventListener
    public void handleUserRegisteredEvent(UserRegisteredEvent event) {
        createDummyTagsForNewUser(event.userId());
    }

    private void createDummyTagsForNewUser(String userId) {
        Tag rootTag = Tag.builder()
            .name("@")
            .userId(userId)
            .embedding(new ArrayList<>())
            .build();
        tagService.saveTag(rootTag);
    }
}
