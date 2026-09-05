package com.ecommerce.rema_baby_crochet.common.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Long id) {
        super("Catégorie introuvable avec l'identifiant : " + id);
    }
}