package com.example.oatnote._commons.message;

public record DeleteAllFilesMessage(
    String userId
) {

    public static DeleteAllFilesMessage of(String userId) {
        return new DeleteAllFilesMessage(userId);
    }
}
