package com.ecommerce.rema_baby_crochet.product.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.ecommerce.rema_baby_crochet.category.dto.CategoryResponse;
import com.ecommerce.rema_baby_crochet.product.ProductStatus;

public record ProductResponse(

        UUID id,
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        String ageRange,
        String material,
        String imageUrl,
        ProductStatus status,
        CategoryResponse category,
        Instant createdAt,
        Instant updatedAt

) {
}
