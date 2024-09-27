package com.example.oatnote.memotag;

import java.util.ArrayList;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.oatnote.event.UserWithdrawEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserWithdrawalListener {

    private final MemoTagService memoTagService;

    @EventListener
    public void handleUserWithdrawalEvent(UserWithdrawEvent event) {
        deleteAllUserData(event.userId());
    }

    private void deleteAllUserData(String userId) {
        memoTagService.deleteAllUserData(userId);
    }
}
