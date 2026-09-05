package com.ecommerce.rema_baby_crochet.product;

import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public final class ProductSpecifications {
     private ProductSpecifications() {}

    public static Specification<Product> hasNameLike(String name) {
        return (root, query, cb) -> StringUtils.hasText(name)
                ? cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%")
                : cb.conjunction();
    }

    public static Specification<Product> hasCategoryId(Long categoryId) {
        return (root, query, cb) -> categoryId != null
                ? cb.equal(root.get("category").get("id"), categoryId)
                : cb.conjunction();
    }

    public static Specification<Product> hasStatus(ProductStatus status) {
        return (root, query, cb) -> status != null
                ? cb.equal(root.get("status"), status)
                : cb.conjunction();
    }


    public static Specification<Product> hasMaterial(String material) {
        return (root, query, cb) -> StringUtils.hasText(material)
                ? cb.like(cb.lower(root.get("material")), "%" + material.toLowerCase() + "%")
                : cb.conjunction();
    }


    public static Specification<Product> hasAgeRange(String ageRange) {
        return (root, query, cb) -> StringUtils.hasText(ageRange)
                ? cb.like(cb.lower(root.get("ageRange")), "%" + ageRange.toLowerCase() + "%")
                : cb.conjunction();
    }
}
