package com.example.oatnote._commons.message;

public record DeleteUserAllFilesMessage(
    String userId
) {

    public static DeleteUserAllFilesMessage of(String userId) {
        return new DeleteUserAllFilesMessage(userId);
    }
}
