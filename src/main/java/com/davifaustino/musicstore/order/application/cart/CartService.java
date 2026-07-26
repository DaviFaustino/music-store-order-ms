package com.davifaustino.musicstore.order.application.cart;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.davifaustino.musicstore.order.application.cart.dto.CreateCart;
import com.davifaustino.musicstore.order.domain.cart.Cart;
import com.davifaustino.musicstore.order.infrastructure.outbound.persistence.cart.CartRepository;

@Service
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public UUID createCart(CreateCart createCart) {
        var cart = Cart.create(createCart.userId());
        return cartRepository.save(cart);
    }
}
