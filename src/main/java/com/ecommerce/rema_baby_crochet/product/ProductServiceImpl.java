package com.ecommerce.rema_baby_crochet.product;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.rema_baby_crochet.category.Category;
import com.ecommerce.rema_baby_crochet.category.CategoryRepository;
import com.ecommerce.rema_baby_crochet.common.exception.CategoryNotFoundException;
import com.ecommerce.rema_baby_crochet.common.exception.ProductNotFoundException;
import com.ecommerce.rema_baby_crochet.product.dto.ProductCreateRequest;
import com.ecommerce.rema_baby_crochet.product.dto.ProductResponse;
import com.ecommerce.rema_baby_crochet.product.dto.ProductUpdateRequest;

@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository,
            ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    @Override
    @Transactional
    public ProductResponse create(ProductCreateRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(request.categoryId()));

        System.out.println("=== REQUEST ===");
        System.out.println("ageRange       = " + request.ageRange());
        System.out.println("stockQuantity = " + request.stockQuantity());
        Product product = new Product(
                request.name(),
                request.description(),
                request.price(),
                request.stockQuantity(),
                request.ageRange(),
                request.material(),
                request.imageUrl(),
                request.status(),

                category);

   
        System.out.println("stockQuantity = " + product.getStockQuantity());

        return productMapper.toResponse(productRepository.save(product));
    }

    @Override
    public ProductResponse getById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return productMapper.toResponse(product);
    }

    @Override
    public Page<ProductResponse> search(String name, Long categoryId,
    ProductStatus status, String material, String ageRange, Pageable pageable) {
    Specification<Product> spec = Specification
    .where(ProductSpecifications.hasNameLike(name))
    .and(ProductSpecifications.hasCategoryId(categoryId))
    .and(ProductSpecifications.hasStatus(status))
    .and(ProductSpecifications.hasMaterial(material))
    .and(ProductSpecifications.hasAgeRange(ageRange));

    return productRepository.findAll(spec, pageable)
    .map(productMapper::toResponse);
    }

    @Override
    @Transactional
    public ProductResponse update(UUID id, ProductUpdateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(request.categoryId()));

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStockQuantity(request.stockQuantity());
        product.setMaterial(request.material());
        product.setImageUrl(request.imageUrl());
        product.setStatus(request.status());
        product.setCategory(category);

        // Pas besoin d'appeler save() explicitement : l'entité est managée dans une
        // transaction, Hibernate synchronise automatiquement les changements (dirty
        // checking).
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }

    // @Override
    // @Transactional
    // public List<ProductResponse> findAll() {
    //     return productRepository.findAll().stream()
    //             .map(productMapper::toResponse).toList();
    // }

}
