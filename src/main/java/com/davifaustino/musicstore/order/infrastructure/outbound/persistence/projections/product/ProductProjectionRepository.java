package com.davifaustino.musicstore.order.infrastructure.outbound.persistence.projections.product;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.davifaustino.musicstore.order.application.projections.product.ProductProjectionDto;

@Component
public class ProductProjectionRepository {

    private final MongoProductProjectionRepository mongoProductProjectionRepository;
    private final ProductProjectionMapper productProjectionMapper;

    public ProductProjectionRepository(MongoProductProjectionRepository mongoProductProjectionRepository, ProductProjectionMapper productProjectionMapper) {
        this.mongoProductProjectionRepository = mongoProductProjectionRepository;
        this.productProjectionMapper = productProjectionMapper;
    }

    public void save(ProductProjectionDto productProjectionDto) {
        mongoProductProjectionRepository.save(productProjectionMapper.toEntity(productProjectionDto));
    }

    public Optional<ProductProjectionDto> findById(UUID productId) {
        return mongoProductProjectionRepository.findById(productId)
                .map(productProjectionMapper::toDto);
    }
}
