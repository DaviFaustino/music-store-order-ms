package com.davifaustino.musicstore.order.application.projections.product;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductCreatedEvent(
    UUID productId,
    String sku,
    String name,
    String description,
    BigDecimal amount,
    String currency,
    String status,
    String type) {
}
