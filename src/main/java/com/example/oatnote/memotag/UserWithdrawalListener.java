package com.example.oatnote.memotag;

import java.util.ArrayList;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.oatnote.event.UserRegisteredEvent;
import com.example.oatnote.memotag.service.tag.model.Tag;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserWithdrawalListener {

    private MemoTagService memoTagService;

    @EventListener
    public void handleUserWithdrawalEvent(UserRegisteredEvent event) {
        deleteAllUserData(event.userId());
    }

    private void deleteAllUserData(String userId) {
        memoTagService.deleteAllUserData(userId);
    }
}
