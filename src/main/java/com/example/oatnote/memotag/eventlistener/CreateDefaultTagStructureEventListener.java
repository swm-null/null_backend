package com.example.oatnote.memotag.eventlistener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.oatnote._commons.event.RegisterUserEvent;
import com.example.oatnote.memotag.service.MemoService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateDefaultTagStructureEventListener {

    private final MemoService memoService;

    @EventListener
    public void handleRegisterUserEvent(RegisterUserEvent event) {
        String rootTagName = "@";
        memoService.createDefaultTagStructureForNewUser(rootTagName, event.userId());
    }
}
