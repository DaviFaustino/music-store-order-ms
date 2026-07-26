package com.davifaustino.musicstore.order.integration;

import com.davifaustino.musicstore.order.IntegrationTests;
import com.davifaustino.musicstore.order.domain.cart.CartStatus;
import com.davifaustino.musicstore.order.infrastructure.outbound.persistence.cart.MongoCartRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateCartIntegrationTests extends IntegrationTests {

    @Autowired
    private MongoCartRepository mongoCartRepository;

    @BeforeEach
    void cleanDatabase() {
        mongoCartRepository.deleteAll();
    }

    @Test
    void shouldCreateCart() throws Exception {
        var userId = UUID.randomUUID();
        var request = postCartRequest("""
                {
                  "userId": "%s"
                }
                """.formatted(userId));

        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(200);

        var cartId = UUID.fromString(response.body().replace("\"", ""));
        var cart = mongoCartRepository.findById(cartId);

        assertThat(cart).isPresent();
        assertThat(cart.get().getId()).isEqualTo(cartId);
        assertThat(cart.get().getUserId()).isEqualTo(userId);
        assertThat(cart.get().getStatus()).isEqualTo(CartStatus.ACTIVE);
        assertThat(cart.get().getItems()).isEmpty();
        assertThat(cart.get().getCreatedAt()).isNotNull();
        assertThat(cart.get().getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldReturnBadRequestWhenRequestBodyIsMalformed() throws Exception {
        var request = postCartRequest("""
                {
                  "userId":
                }
                """);

        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(400);
        assertThat(mongoCartRepository.count()).isZero();
    }

    @Test
    void shouldReturnServerErrorWhenUserIdIsMissing() throws Exception {
        var request = postCartRequest("{}");

        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(500);
        assertThat(mongoCartRepository.count()).isZero();
    }

    private HttpRequest postCartRequest(String body) {
        return HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:%d/carts".formatted(port)))
                .header("Content-Type", headers.getContentType().toString())
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
    }
}
