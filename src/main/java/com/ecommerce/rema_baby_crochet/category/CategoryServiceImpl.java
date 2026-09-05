package com.ecommerce.rema_baby_crochet.category;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.rema_baby_crochet.category.dto.CategoryCreateRequest;
import com.ecommerce.rema_baby_crochet.category.dto.CategoryResponse;
import com.ecommerce.rema_baby_crochet.category.dto.CategoryUpdateRequest;
import com.ecommerce.rema_baby_crochet.common.exception.CategoryNotFoundException;

@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    @Transactional
    public CategoryResponse create(CategoryCreateRequest request) {
        Category category = new Category(
            request.name(),
            request.slug()
        );

        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        return categoryMapper.toResponse(category);
    }

    @Override
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryResponse update(Long id, CategoryUpdateRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        category.setName(request.name());
        category.setSlug(request.slug());

        return categoryMapper.toResponse(category);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new CategoryNotFoundException(id);
        }
        categoryRepository.deleteById(id);
    }
}
