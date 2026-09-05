package com.ecommerce.rema_baby_crochet.product.dto;

import com.ecommerce.rema_baby_crochet.product.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductCreateRequest (

    @NotBlank(message = "Le nom du produit est obligatoire")
    @Size(max = 100, message = "Le nom du produit ne peut pas dépasser 100 caractères")
    String name,

    @Size(max = 500, message = "La description du produit ne peut pas dépasser 500 caractères")
    String description,

    @NotNull(message = "Le prix du produit est obligatoire")
    @Positive(message = "Le prix du produit doit être un nombre positif")
    BigDecimal price,

    @NotNull(message = "La quantité en stock est obligatoire")
    @PositiveOrZero(message = "La quantité en stock doit être un nombre positif ")
    Integer stockQuantity,

    @NotBlank (message= "L'intervalle d'âge est obligatoire")
    @Size(max = 100, message = "L'intervalle d'âge ne peut pas dépasser 100 caractères")
    String ageRange,

    @NotBlank (message= "La matière est obligatoire")
    @Size(max = 100, message = "La matière ne peut pas dépasser 100 caractères")
    String material,

    @Size(max = 500, message = "L'URL de l'image ne peut pas dépasser 500 caractères")
    String imageUrl,


    @NotNull(message = "Le statut du produit est obligatoire")
    ProductStatus status,


    @NotNull(message = "L'identifiant de la catégorie est obligatoire")
    Long categoryId



){}
