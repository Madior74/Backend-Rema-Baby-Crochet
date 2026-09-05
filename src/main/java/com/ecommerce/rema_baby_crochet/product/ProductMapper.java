package com.ecommerce.rema_baby_crochet.product;

import com.ecommerce.rema_baby_crochet.category.Category;
import com.ecommerce.rema_baby_crochet.category.dto.CategoryResponse;
import com.ecommerce.rema_baby_crochet.product.dto.ProductResponse;

public interface ProductMapper {

    default ProductResponse toResponse(Product product) {
        if (product == null) {
            return null;
        }
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getAgeRange(),
                product.getMaterial(),
                product.getImageUrl(),
                product.getStatus(),
                toCategoryResponse(product.getCategory()),
                product.getCreatedAt(),
                product.getUpdatedAt()

        );
    }

    default CategoryResponse toCategoryResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getSlug());
    }

}
