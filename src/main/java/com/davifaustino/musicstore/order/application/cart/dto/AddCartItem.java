package com.davifaustino.musicstore.order.application.cart.dto;

import java.util.UUID;

public record AddCartItem(
    UUID productId,
    int quantity) {
}
