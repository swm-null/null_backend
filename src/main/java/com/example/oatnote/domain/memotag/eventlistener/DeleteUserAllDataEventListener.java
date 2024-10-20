package com.example.oatnote.domain.memotag.eventlistener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.oatnote.event.WithdrawUserEvent;
import com.example.oatnote.domain.memotag.MemoTagService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeleteUserAllDataEventListener {

    private final MemoTagService memoTagService;

    @EventListener
    public void handleWithdrawUserEvent(WithdrawUserEvent event) {
        memoTagService.deleteUserAllData(event.userId());
    }
}
