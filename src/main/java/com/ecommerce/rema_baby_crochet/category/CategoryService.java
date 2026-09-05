package com.ecommerce.rema_baby_crochet.category;

import com.ecommerce.rema_baby_crochet.category.dto.CategoryCreateRequest;
import com.ecommerce.rema_baby_crochet.category.dto.CategoryResponse;
import com.ecommerce.rema_baby_crochet.category.dto.CategoryUpdateRequest;

public interface CategoryService {

    CategoryResponse create(CategoryCreateRequest request);

    CategoryResponse getById(Long id);

    java.util.List<CategoryResponse> findAll();

    CategoryResponse update(Long id, CategoryUpdateRequest request);

    void delete(Long id);

}
