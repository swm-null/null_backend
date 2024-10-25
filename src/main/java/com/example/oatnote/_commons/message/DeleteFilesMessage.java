package com.example.oatnote._commons.message;

import java.util.List;

public record DeleteFilesMessage(
    List<String> imageUrls,
    String userId
) {

    public static DeleteFilesMessage of(List<String> imageUrls, String userId) {
        return new DeleteFilesMessage(imageUrls, userId);
    }
}
