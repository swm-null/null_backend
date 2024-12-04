package com.example.oatnote.memotag.eventlistener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.oatnote._commons.event.RegisterUserEvent;
import com.example.oatnote.memotag.service.MemoTagService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateDefaultTagStructureEventListener {

    private final MemoTagService memoTagService;

    @EventListener
    public void handleRegisterUserEvent(RegisterUserEvent event) {
        String rootTagName = "@";
        memoTagService.createDefaultTagStructureForNewUser(rootTagName, event.userId());
    }
}
