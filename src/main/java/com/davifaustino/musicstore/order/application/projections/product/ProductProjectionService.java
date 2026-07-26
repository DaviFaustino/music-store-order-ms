package com.davifaustino.musicstore.order.application.projections.product;

import org.springframework.stereotype.Service;

import com.davifaustino.musicstore.order.infrastructure.outbound.persistence.projections.product.ProductProjectionRepository;

@Service
public class ProductProjectionService {

    private final ProductProjectionRepository productProjectionRepository;

    public ProductProjectionService(ProductProjectionRepository productProjectionRepository) {
        this.productProjectionRepository = productProjectionRepository;
    }

    public void save(ProductCreatedEvent productCreatedEvent) {
        productProjectionRepository.save(
            new ProductProjectionDto(
                productCreatedEvent.productId(),
                productCreatedEvent.name(),
                productCreatedEvent.amount(),
                productCreatedEvent.currency(),
                productCreatedEvent.status()
            )
        );
    }
}
