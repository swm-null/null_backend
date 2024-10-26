package com.example.oatnote._commons.message;

import java.util.List;

public record DeleteFilesMessage(
    List<String> fileUrls,
    String userId
) {

    public static DeleteFilesMessage of(List<String> fileUrls, String userId) {
        return new DeleteFilesMessage(fileUrls, userId);
    }
}
