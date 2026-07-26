package com.davifaustino.musicstore.order.application.projections.product;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductProjectionDto(
    UUID productId,
    String name,
    BigDecimal amount,
    String currency,
    String status) {
}
