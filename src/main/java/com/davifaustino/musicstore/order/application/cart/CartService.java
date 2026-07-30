package com.davifaustino.musicstore.order.application.cart;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.davifaustino.musicstore.order.application.cart.dto.AddCartItem;
import com.davifaustino.musicstore.order.application.cart.dto.CreateCart;
import com.davifaustino.musicstore.order.application.projections.product.ProductProjectionService;
import com.davifaustino.musicstore.order.domain.cart.Cart;
import com.davifaustino.musicstore.order.domain.cart.CartItem;
import com.davifaustino.musicstore.order.domain.cart.Money;
import com.davifaustino.musicstore.order.infrastructure.outbound.persistence.cart.CartRepository;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductProjectionService productProjectionService;

    public CartService(CartRepository cartRepository, ProductProjectionService productProjectionService) {
        this.cartRepository = cartRepository;
        this.productProjectionService = productProjectionService;
    }

    public UUID createCart(CreateCart createCart) {
        var cart = Cart.create(createCart.userId());
        return cartRepository.save(cart);
    }

    public void addCartItem(UUID cartId, AddCartItem addCartItem) {
        var cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        var productProjection = productProjectionService.findById(addCartItem.productId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (!productProjection.status().equals("ACTIVE")) {
            throw new IllegalArgumentException("Product is not active");
        }
        cart.addItem(new CartItem(
            productProjection.productId(),
            productProjection.name(),
            new Money(productProjection.amount(), productProjection.currency()),
            addCartItem.quantity()
        ));
        cartRepository.save(cart);
    }
}
