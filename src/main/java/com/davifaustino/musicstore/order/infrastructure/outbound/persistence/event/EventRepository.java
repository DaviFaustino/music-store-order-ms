package com.davifaustino.musicstore.order.infrastructure.outbound.persistence.event;

import org.springframework.stereotype.Component;

import com.davifaustino.musicstore.order.application.event.EventDto;

@Component
public class EventRepository {

    private final MongoEventRepository mongoEventRepository;
    private final EventMapper eventMapper;

    public EventRepository(MongoEventRepository mongoEventRepository, EventMapper eventMapper) {
        this.mongoEventRepository = mongoEventRepository;
        this.eventMapper = eventMapper;
    }

    public boolean saveIfNew(EventDto event) {
        if (mongoEventRepository.existsById(event.id())) {
            return false;
        }

        try {
            mongoEventRepository.save(eventMapper.toEntity(event));
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
}
