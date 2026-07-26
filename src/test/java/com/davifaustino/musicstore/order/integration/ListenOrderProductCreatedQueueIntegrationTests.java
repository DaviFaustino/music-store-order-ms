package com.davifaustino.musicstore.order.integration;

import com.davifaustino.musicstore.order.IntegrationTests;
import com.davifaustino.musicstore.order.application.event.EventDto;
import com.davifaustino.musicstore.order.infrastructure.config.RabbitMQConfig;
import com.davifaustino.musicstore.order.infrastructure.outbound.persistence.event.MongoEventRepository;
import com.davifaustino.musicstore.order.infrastructure.outbound.persistence.projections.product.MongoProductProjectionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ListenOrderProductCreatedQueueIntegrationTests extends IntegrationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MongoEventRepository mongoEventRepository;

    @Autowired
    private MongoProductProjectionRepository mongoProductProjectionRepository;

    @BeforeEach
    void cleanState() {
        rabbitTemplate.execute(channel -> {
            channel.queuePurge(RabbitMQConfig.PRODUCT_CREATED_QUEUE_NAME);
            return null;
        });
        mongoEventRepository.deleteAll();
        mongoProductProjectionRepository.deleteAll();
    }

    @Test
    void shouldSaveProductProjectionWhenProductCreatedEventIsReceived() throws Exception {
        var productId = UUID.randomUUID();
        var eventId = UUID.randomUUID();
        var correlationId = UUID.randomUUID();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PRODUCT_CREATED_QUEUE_NAME,
                productCreatedEvent(eventId, correlationId, productId, "Kind of Blue", "39.90", "USD", "ACTIVE")
        );

        awaitAssertion(() -> {
            assertThat(mongoEventRepository.findById(eventId)).isPresent();

            var projection = mongoProductProjectionRepository.findById(productId);
            assertThat(projection).isPresent();
            assertThat(projection.get().getProductId()).isEqualTo(productId);
            assertThat(projection.get().getName()).isEqualTo("Kind of Blue");
            assertThat(projection.get().getAmount()).isEqualByComparingTo(new BigDecimal("39.90"));
            assertThat(projection.get().getCurrency()).isEqualTo("USD");
            assertThat(projection.get().getStatus()).isEqualTo("ACTIVE");
        });
    }

    @Test
    void shouldSkipProductProjectionWhenEventWasAlreadyProcessed() throws Exception {
        var eventId = UUID.randomUUID();
        var correlationId = UUID.randomUUID();
        var firstProductId = UUID.randomUUID();
        var duplicateProductId = UUID.randomUUID();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PRODUCT_CREATED_QUEUE_NAME,
                productCreatedEvent(eventId, correlationId, firstProductId, "Blue Train", "49.90", "USD", "ACTIVE")
        );

        awaitAssertion(() -> {
            assertThat(mongoEventRepository.count()).isEqualTo(1);
            assertThat(mongoProductProjectionRepository.findById(firstProductId)).isPresent();
        });

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PRODUCT_CREATED_QUEUE_NAME,
                productCreatedEvent(eventId, correlationId, duplicateProductId, "Duplicate", "9.90", "USD", "INACTIVE")
        );

        awaitAssertion(() -> assertThat(queueMessageCount()).isZero());

        assertThat(mongoEventRepository.count()).isEqualTo(1);
        assertThat(mongoProductProjectionRepository.count()).isEqualTo(1);
        assertThat(mongoProductProjectionRepository.findById(firstProductId)).isPresent();
        assertThat(mongoProductProjectionRepository.findById(duplicateProductId)).isEmpty();
    }

    private EventDto productCreatedEvent(
            UUID eventId,
            UUID correlationId,
            UUID productId,
            String name,
            String amount,
            String currency,
            String status
    ) {
        return new EventDto(
                eventId,
                correlationId,
                "PRODUCT_CREATED",
                """
                {
                    "productId": "%s",
                    "sku": "SKU-%s",
                    "name": "%s",
                    "description": "Integration test product",
                    "amount": %s,
                    "currency": "%s",
                    "status": "%s",
                    "type": "VINYL"
                }
                """.formatted(productId, productId.toString().substring(0, 8), name, amount, currency, status)
        );
    }

    private int queueMessageCount() {
        return rabbitTemplate.execute(channel ->
                channel.queueDeclarePassive(RabbitMQConfig.PRODUCT_CREATED_QUEUE_NAME).getMessageCount()
        );
    }

    private void awaitAssertion(CheckedAssertion assertion) throws Exception {
        AssertionError lastAssertionError = null;
        long deadline = System.nanoTime() + 5_000_000_000L;

        while (System.nanoTime() < deadline) {
            try {
                assertion.run();
                return;
            } catch (AssertionError assertionError) {
                lastAssertionError = assertionError;
                Thread.sleep(100);
            }
        }

        if (lastAssertionError != null) {
            throw lastAssertionError;
        }
    }

    @FunctionalInterface
    private interface CheckedAssertion {
        void run() throws Exception;
    }
}
