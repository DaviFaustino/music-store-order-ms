package com.davifaustino.musicstore.order.infrastructure.outbound.persistence.cart;

import org.springframework.stereotype.Component;

import com.davifaustino.musicstore.order.domain.cart.Cart;

@Component
public class CartPersistenceMapper {

    public CartEntity toEntity(Cart cart) {
        return new CartEntity(
            cart.getId(),
            cart.getUserId(),
            cart.getStatus(),
            cart.getItems(),
            cart.getCreatedAt(),
            cart.getUpdatedAt()
        );
    }

    public Cart toDomain(CartEntity cartEntity) {
        return new Cart(
            cartEntity.getId(),
            cartEntity.getUserId(),
            cartEntity.getStatus(),
            cartEntity.getItems(),
            cartEntity.getCreatedAt(),
            cartEntity.getUpdatedAt()
        );
    }
}
