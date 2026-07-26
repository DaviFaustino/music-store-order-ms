package com.davifaustino.musicstore.order.infrastructure.inbound.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.davifaustino.musicstore.order.application.event.EventDto;
import com.davifaustino.musicstore.order.application.event.EventService;
import com.davifaustino.musicstore.order.application.projections.product.ProductProjectionService;
import com.davifaustino.musicstore.order.application.projections.product.ProductCreatedEvent;
import static com.davifaustino.musicstore.order.infrastructure.config.RabbitMQConfig.PRODUCT_CREATED_QUEUE_NAME;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Component
public class OrderConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderConsumer.class);

    private final EventService eventService;
    private final ObjectMapper objectMapper;
    private final ProductProjectionService productProjectionService;

    public OrderConsumer(EventService eventService, ObjectMapper objectMapper, ProductProjectionService productProjectionService) {
        this.eventService = eventService;
        this.objectMapper = objectMapper;
        this.productProjectionService = productProjectionService;
    }

    @RabbitListener(queues = PRODUCT_CREATED_QUEUE_NAME)
    public void listenOrderProductCreatedQueue(EventDto event) throws JacksonException {
        if (!eventService.saveIfNew(event)) {
            LOGGER.info("Event {} already processed, skipping", event.id());
            return;
        }

        var productCreatedEvent = objectMapper.readValue(event.payload(), ProductCreatedEvent.class);
        productProjectionService.save(productCreatedEvent);
    }
}
