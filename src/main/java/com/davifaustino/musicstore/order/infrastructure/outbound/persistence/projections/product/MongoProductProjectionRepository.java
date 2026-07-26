package com.davifaustino.musicstore.order.infrastructure.outbound.persistence.projections.product;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoProductProjectionRepository extends MongoRepository<ProductProjectionEntity, UUID> {
}
