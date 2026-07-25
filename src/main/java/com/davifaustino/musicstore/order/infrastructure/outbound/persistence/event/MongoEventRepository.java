package com.davifaustino.musicstore.order.infrastructure.outbound.persistence.event;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoEventRepository extends MongoRepository<EventEntity, UUID> {
}
