package com.ecommerce.rema_baby_crochet.category;

import org.springframework.stereotype.Component;

import com.ecommerce.rema_baby_crochet.category.dto.CategoryResponse;

@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public CategoryResponse toResponse(Category category) {
        if (category == null) {
            return null;
        }
        return new CategoryResponse(
            category.getId(),
            category.getName(),
            category.getSlug()
        );
    }

}
