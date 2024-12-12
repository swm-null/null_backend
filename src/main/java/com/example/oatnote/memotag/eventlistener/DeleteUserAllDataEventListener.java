package com.example.oatnote.memotag.eventlistener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.oatnote._commons.event.WithdrawUserEvent;
import com.example.oatnote.memotag.service.MemoService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeleteUserAllDataEventListener {

    private final MemoService memoService;

    @EventListener
    public void handleWithdrawUserEvent(WithdrawUserEvent event) {
        memoService.deleteUserAllData(event.userId());
    }
}
