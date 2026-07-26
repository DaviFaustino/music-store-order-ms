package com.davifaustino.musicstore.order.infrastructure.outbound.persistence.projections.product;

import org.springframework.stereotype.Component;

import com.davifaustino.musicstore.order.application.projections.product.ProductProjectionDto;

@Component
public class ProductProjectionMapper {
    public ProductProjectionEntity toEntity(ProductProjectionDto productProjectionDto) {
        return new ProductProjectionEntity(
                productProjectionDto.productId(),
                productProjectionDto.name(),
                productProjectionDto.amount(),
                productProjectionDto.currency(),
                productProjectionDto.status()
        );
    }

    public ProductProjectionDto toDto(ProductProjectionEntity productProjectionEntity) {
        return new ProductProjectionDto(
                productProjectionEntity.getProductId(),
                productProjectionEntity.getName(),
                productProjectionEntity.getAmount(),
                productProjectionEntity.getCurrency(),
                productProjectionEntity.getStatus()
        );
    }
}
