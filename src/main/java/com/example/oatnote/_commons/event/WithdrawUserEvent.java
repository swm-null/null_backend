package com.example.oatnote._commons.event;

public record WithdrawUserEvent(
    String userId
) {

    public static WithdrawUserEvent of(String userId) {
        return new WithdrawUserEvent(userId);
    }
}
