package com.davifaustino.musicstore.order.infrastructure.outbound.persistence.cart;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoCartRepository extends MongoRepository<CartEntity, UUID> {
}
