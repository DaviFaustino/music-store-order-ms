package com.davifaustino.musicstore.order.domain.cart;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Cart {

    private UUID id;
    private UUID userId;
    private CartStatus status;
    private List<CartItem> items;
    private Instant createdAt;
    private Instant updatedAt;

    public Cart(UUID id, UUID userId, CartStatus status, List<CartItem> items, Instant createdAt, Instant updatedAt) {

        if (userId == null) {
            throw new IllegalArgumentException("User id is required");
        }

        this.id = id;
        this.userId = userId;
        this.status = status;
        this.items = items != null ? items : new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Cart create(UUID userId) {
        Instant now = Instant.now();

        return new Cart(
                UUID.randomUUID(),
                userId,
                CartStatus.ACTIVE,
                new ArrayList<>(),
                now,
                now
        );
    }

    public void addItem(CartItem cartItem) {
        if (cartItem == null) {
            throw new IllegalArgumentException("Cart item is required");
        }
        if (cartItem.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        if (this.status != CartStatus.ACTIVE) {
            throw new IllegalStateException("Cannot add items to a cart that is not active");
        }

        CartItem existingItem = items.stream()
                .filter(item -> item.getProductId().equals(cartItem.getProductId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.increaseQuantity(cartItem.getQuantity());
        } else {
            items.add(cartItem);
        }

        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public UUID getUserId() {
        return userId;
    }
    public void setUserId(UUID userId) {
        this.userId = userId;
    }
    public CartStatus getStatus() {
        return status;
    }
    public void setStatus(CartStatus status) {
        this.status = status;
    }
    public List<CartItem> getItems() {
        return items;
    }
    public void setItems(List<CartItem> items) {
        this.items = items;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
    public Instant getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
