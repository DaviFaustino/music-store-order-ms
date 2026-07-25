package com.davifaustino.musicstore.order.infrastructure.outbound.persistence.event;

import org.springframework.stereotype.Component;

import com.davifaustino.musicstore.order.application.event.EventDto;

@Component
public class EventMapper {

    public EventEntity toEntity(EventDto eventDto) {
        return new EventEntity(
                eventDto.id(),
                eventDto.correlationId(),
                eventDto.eventType(),
                eventDto.payload()
        );
    }

    public EventDto toDto(EventEntity eventEntity) {
        return new EventDto(
                eventEntity.getId(),
                eventEntity.getCorrelationId(),
                eventEntity.getEventType(),
                eventEntity.getPayload()
        );
    }
}
