package com.davifaustino.musicstore.order.domain.cart;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(BigDecimal amount, String currency) {

    private static final int MONEY_SCALE = 2;

    public Money {
        Objects.requireNonNull(amount, "Amount is required");

        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Currency is required");
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }

        amount = amount.setScale(MONEY_SCALE, RoundingMode.HALF_UP);
        currency = currency.toUpperCase();
    }
}