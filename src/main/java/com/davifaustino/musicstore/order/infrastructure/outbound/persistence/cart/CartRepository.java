package com.davifaustino.musicstore.order.infrastructure.outbound.persistence.cart;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.davifaustino.musicstore.order.domain.cart.Cart;

@Component
public class CartRepository {

    private final MongoCartRepository mongoCartRepository;
    private final CartPersistenceMapper cartPersistenceMapper;

    public CartRepository(MongoCartRepository mongoCartRepository, CartPersistenceMapper cartPersistenceMapper) {
        this.mongoCartRepository = mongoCartRepository;
        this.cartPersistenceMapper = cartPersistenceMapper;
    }

    public UUID save(Cart cart) {
        CartEntity cartEntity = cartPersistenceMapper.toEntity(cart);
        return mongoCartRepository.save(cartEntity).getId();
    }

    public Optional<Cart> findById(UUID cartId) {
        return mongoCartRepository.findById(cartId)
                .map(cartPersistenceMapper::toDomain);
    }
}
