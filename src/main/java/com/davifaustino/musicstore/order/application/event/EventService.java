package com.davifaustino.musicstore.order.application.event;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.davifaustino.musicstore.order.infrastructure.outbound.persistence.event.EventRepository;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public boolean saveIfNew(EventDto event) {
        try {
            return eventRepository.saveIfNew(event);
        } catch (DataIntegrityViolationException exception) {
            return false;
        }
    }
}
