package com.davifaustino.musicstore.order.integration;

import com.davifaustino.musicstore.order.IntegrationTests;
import com.davifaustino.musicstore.order.domain.cart.CartItem;
import com.davifaustino.musicstore.order.domain.cart.CartStatus;
import com.davifaustino.musicstore.order.domain.cart.Money;
import com.davifaustino.musicstore.order.infrastructure.outbound.persistence.cart.CartEntity;
import com.davifaustino.musicstore.order.infrastructure.outbound.persistence.cart.MongoCartRepository;
import com.davifaustino.musicstore.order.infrastructure.outbound.persistence.projections.product.MongoProductProjectionRepository;
import com.davifaustino.musicstore.order.infrastructure.outbound.persistence.projections.product.ProductProjectionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class AddCartItemIntegrationTests extends IntegrationTests {

    @Autowired
    private MongoCartRepository mongoCartRepository;

    @Autowired
    private MongoProductProjectionRepository mongoProductProjectionRepository;

    @BeforeEach
    void cleanDatabase() {
        mongoCartRepository.deleteAll();
        mongoProductProjectionRepository.deleteAll();
    }

    @Test
    void shouldAddCartItem() throws Exception {
        var cartId = UUID.randomUUID();
        var productId = UUID.randomUUID();
        saveCart(cartId);
        saveProductProjection(productId, "Kind of Blue", "39.90", "USD", "ACTIVE");

        var response = httpClient.send(
                postCartItemRequest(cartId, addCartItemBody(productId, 2)),
                HttpResponse.BodyHandlers.ofString()
        );

        assertThat(response.statusCode()).isEqualTo(200);

        var cart = mongoCartRepository.findById(cartId);
        assertThat(cart).isPresent();
        assertThat(cart.get().getItems()).hasSize(1);

        var item = cart.get().getItems().getFirst();
        assertThat(item.getProductId()).isEqualTo(productId);
        assertThat(item.getName()).isEqualTo("Kind of Blue");
        assertThat(item.getUnitPrice().amount()).isEqualByComparingTo(new BigDecimal("39.90"));
        assertThat(item.getUnitPrice().currency()).isEqualTo("USD");
        assertThat(item.getQuantity()).isEqualTo(2);
        assertThat(cart.get().getUpdatedAt()).isAfter(cart.get().getCreatedAt());
    }

    @Test
    void shouldIncrementQuantityWhenProductAlreadyExistsInCart() throws Exception {
        var cartId = UUID.randomUUID();
        var productId = UUID.randomUUID();
        saveCart(
                cartId,
                new CartItem(productId, "Kind of Blue", new Money(new BigDecimal("39.90"), "USD"), 2)
        );
        saveProductProjection(productId, "Kind of Blue", "39.90", "USD", "ACTIVE");

        var response = httpClient.send(
                postCartItemRequest(cartId, addCartItemBody(productId, 3)),
                HttpResponse.BodyHandlers.ofString()
        );

        assertThat(response.statusCode()).isEqualTo(200);

        var cart = mongoCartRepository.findById(cartId);
        assertThat(cart).isPresent();
        assertThat(cart.get().getItems()).hasSize(1);
        assertThat(cart.get().getItems().getFirst().getQuantity()).isEqualTo(5);
    }

    @Test
    void shouldReturnBadRequestWhenRequestBodyIsMalformed() throws Exception {
        var cartId = UUID.randomUUID();
        saveCart(cartId);

        var response = httpClient.send(
                postCartItemRequest(cartId, """
                        {
                          "productId":
                        }
                        """),
                HttpResponse.BodyHandlers.ofString()
        );

        assertThat(response.statusCode()).isEqualTo(400);
    }

    @Test
    void shouldReturnServerErrorWhenCartDoesNotExist() throws Exception {
        var productId = UUID.randomUUID();
        saveProductProjection(productId, "Kind of Blue", "39.90", "USD", "ACTIVE");

        var response = httpClient.send(
                postCartItemRequest(UUID.randomUUID(), addCartItemBody(productId, 1)),
                HttpResponse.BodyHandlers.ofString()
        );

        assertThat(response.statusCode()).isEqualTo(500);
        assertThat(mongoCartRepository.count()).isZero();
    }

    @Test
    void shouldReturnServerErrorWhenProductDoesNotExist() throws Exception {
        var cartId = UUID.randomUUID();
        saveCart(cartId);

        var response = httpClient.send(
                postCartItemRequest(cartId, addCartItemBody(UUID.randomUUID(), 1)),
                HttpResponse.BodyHandlers.ofString()
        );

        assertThat(response.statusCode()).isEqualTo(500);
        assertThat(mongoCartRepository.findById(cartId).get().getItems()).isEmpty();
    }

    @Test
    void shouldReturnServerErrorWhenProductIsInactive() throws Exception {
        var cartId = UUID.randomUUID();
        var productId = UUID.randomUUID();
        saveCart(cartId);
        saveProductProjection(productId, "Kind of Blue", "39.90", "USD", "INACTIVE");

        var response = httpClient.send(
                postCartItemRequest(cartId, addCartItemBody(productId, 1)),
                HttpResponse.BodyHandlers.ofString()
        );

        assertThat(response.statusCode()).isEqualTo(500);
        assertThat(mongoCartRepository.findById(cartId).get().getItems()).isEmpty();
    }

    @Test
    void shouldReturnServerErrorWhenQuantityIsNotPositive() throws Exception {
        var cartId = UUID.randomUUID();
        var productId = UUID.randomUUID();
        saveCart(cartId);
        saveProductProjection(productId, "Kind of Blue", "39.90", "USD", "ACTIVE");

        var response = httpClient.send(
                postCartItemRequest(cartId, addCartItemBody(productId, 0)),
                HttpResponse.BodyHandlers.ofString()
        );

        assertThat(response.statusCode()).isEqualTo(500);
        assertThat(mongoCartRepository.findById(cartId).get().getItems()).isEmpty();
    }

    private void saveCart(UUID cartId, CartItem... items) {
        var now = Instant.now();
        mongoCartRepository.save(new CartEntity(
                cartId,
                UUID.randomUUID(),
                CartStatus.ACTIVE,
                new ArrayList<>(List.of(items)),
                now,
                now
        ));
    }

    private void saveProductProjection(UUID productId, String name, String amount, String currency, String status) {
        mongoProductProjectionRepository.save(new ProductProjectionEntity(
                productId,
                name,
                new BigDecimal(amount),
                currency,
                status
        ));
    }

    private HttpRequest postCartItemRequest(UUID cartId, String body) {
        return HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:%d/carts/%s/items".formatted(port, cartId)))
                .header("Content-Type", headers.getContentType().toString())
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
    }

    private String addCartItemBody(UUID productId, int quantity) {
        return """
                {
                  "productId": "%s",
                  "quantity": %d
                }
                """.formatted(productId, quantity);
    }
}
