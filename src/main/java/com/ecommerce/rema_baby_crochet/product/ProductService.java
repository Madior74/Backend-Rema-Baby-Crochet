package com.ecommerce.rema_baby_crochet.product;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ecommerce.rema_baby_crochet.product.dto.ProductCreateRequest;
import com.ecommerce.rema_baby_crochet.product.dto.ProductResponse;
import com.ecommerce.rema_baby_crochet.product.dto.ProductUpdateRequest;

public interface ProductService {

    ProductResponse create(ProductCreateRequest request);

    ProductResponse getById(UUID id);

    // List<ProductResponse> findAll();


    Page<ProductResponse> search(String name, Long categoryId, ProductStatus status, String material, String ageRange, Pageable pageable);

    ProductResponse update(UUID id, ProductUpdateRequest request);

    void delete(UUID id);

    
}
