package com.example.oatnote.domain.memotag.eventlistener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.oatnote.domain.memotag.service.MemoTagService;
import com.example.oatnote.event.RegisterUserEvent;
import com.example.oatnote.domain.memotag.service.tag.TagService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateDefaultTagStructureEventListener {

    private final TagService tagService;
    private final MemoTagService memoTagService;

    @EventListener
    public void handleRegisterUserEvent(RegisterUserEvent event) {
        String rootTagName = "@";
        memoTagService.createDefaultTagStructureForNewUser(rootTagName, event.userId());
    }
}
