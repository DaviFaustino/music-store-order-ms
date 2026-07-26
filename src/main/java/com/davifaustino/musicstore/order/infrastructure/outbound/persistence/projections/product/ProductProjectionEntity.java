package com.davifaustino.musicstore.order.infrastructure.outbound.persistence.projections.product;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "product_projection")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductProjectionEntity {

    @Id
    private UUID productId;
    private String name;
    private BigDecimal amount;
    private String currency;
    private String status;
}
