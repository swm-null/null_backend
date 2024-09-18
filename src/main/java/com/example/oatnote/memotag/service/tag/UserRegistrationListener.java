package com.example.oatnote.memotag.service.tag;

import java.util.ArrayList;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.oatnote.event.UserRegisteredEvent;
import com.example.oatnote.memotag.service.tag.model.Tag;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserRegistrationListener {

    private final TagService tagService;

    @EventListener
    public void handleUserRegistrationEvent(UserRegisteredEvent event) {
        createRootTag(event.userId());
    }

    private void createRootTag(String userId) {
        Tag rootTag = new Tag(
            userId,
            "@",
            userId,
            new ArrayList<>()
        );
        tagService.saveTag(rootTag);
    }
}
