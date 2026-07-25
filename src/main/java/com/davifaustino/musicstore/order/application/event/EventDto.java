package com.davifaustino.musicstore.order.application.event;

import java.util.UUID;

public record EventDto(
    UUID id,
    UUID correlationId,
    String eventType,
    String payload) {
}
