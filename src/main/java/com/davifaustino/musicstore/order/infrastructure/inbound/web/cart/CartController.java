package com.davifaustino.musicstore.order.infrastructure.inbound.web.cart;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.davifaustino.musicstore.order.application.cart.CartService;
import com.davifaustino.musicstore.order.application.cart.dto.CreateCart;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<UUID> createCart(@RequestBody CreateCart createCart) {
        var cartId = cartService.createCart(createCart);
        return ResponseEntity.ok(cartId);
    }
}
