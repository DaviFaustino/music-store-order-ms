package com.davifaustino.musicstore.order.domain.cart;

import java.util.UUID;

public class CartItem {

    private UUID productId;
    private String name;
    private Money unitPrice;
    private int quantity;

    public CartItem(UUID productId, String name, Money unitPrice, int quantity) {
        if (productId == null) {
            throw new IllegalArgumentException("Product id is required");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name is required");
        }
        if (unitPrice == null) {
            throw new IllegalArgumentException("Unit price is required");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        this.productId = productId;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public void increaseQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        this.quantity += quantity;
    }

    public UUID getProductId() {
        return productId;
    }
    public void setProductId(UUID productId) {
        this.productId = productId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Money getUnitPrice() {
        return unitPrice;
    }
    public void setUnitPrice(Money unitPrice) {
        this.unitPrice = unitPrice;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
