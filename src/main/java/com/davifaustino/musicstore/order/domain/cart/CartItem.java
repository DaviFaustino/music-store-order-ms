package com.davifaustino.musicstore.order.domain.cart;

import java.util.UUID;

public class CartItem {

    private UUID productId;
    private String name;
    private Money unitPrice;
    private int quantity;

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
