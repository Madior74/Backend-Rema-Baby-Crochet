package com.ecommerce.rema_baby_crochet.category;

import com.ecommerce.rema_baby_crochet.category.dto.CategoryResponse;

public interface CategoryMapper {

    CategoryResponse toResponse(Category category);

}
