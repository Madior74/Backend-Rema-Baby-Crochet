package com.ecommerce.rema_baby_crochet.product;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductRepository extends JpaRepository<Product, UUID>,JpaSpecificationExecutor<Product>{
    
}
