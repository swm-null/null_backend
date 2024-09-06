package com.example.oatnote.event.model;

import java.util.UUID;

public record UserRegisteredEvent(
    UUID userId
) {

}
