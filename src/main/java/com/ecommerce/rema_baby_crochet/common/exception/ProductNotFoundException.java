package com.ecommerce.rema_baby_crochet.common.exception;

import java.util.UUID;

public class ProductNotFoundException  extends RuntimeException{

    public ProductNotFoundException(UUID id){
        super("Produit introuvable avec l'identifiant :"+ id);
    }
    
}
