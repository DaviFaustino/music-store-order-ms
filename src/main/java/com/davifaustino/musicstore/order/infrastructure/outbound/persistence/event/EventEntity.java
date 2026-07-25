package com.davifaustino.musicstore.order.infrastructure.outbound.persistence.event;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventEntity {

    @Id
    private UUID id;
    private UUID correlationId;
    private String eventType;
    private String payload;
}
