package com.example.oatnote.domain.memotag.eventlistener;

import java.util.ArrayList;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.oatnote.event.RegisterUserEvent;
import com.example.oatnote.domain.memotag.service.tag.TagService;
import com.example.oatnote.domain.memotag.service.tag.model.Tag;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateRootTagEventListener {

    private final TagService tagService;

    @EventListener
    public void handleRegisterUserEvent(RegisterUserEvent event) {
        Tag rootTag = new Tag(
            event.userId(),
            "@",
            event.userId(),
            new ArrayList<>()
        );
        tagService.createTag(rootTag);
    }
}
