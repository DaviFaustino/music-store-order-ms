package com.davifaustino.musicstore.order.infrastructure.outbound.persistence.cart;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.davifaustino.musicstore.order.domain.cart.CartItem;
import com.davifaustino.musicstore.order.domain.cart.CartStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartEntity {

    @Id
    private UUID id;
    private UUID userId;
    private CartStatus status;
    private List<CartItem> items;
    private Instant createdAt;
    private Instant updatedAt;
}
