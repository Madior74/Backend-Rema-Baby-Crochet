package com.ecommerce.rema_baby_crochet.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryUpdateRequest(

    @NotBlank(message = "Le nom de la catégorie est obligatoire")
    @Size(max = 80, message = "Le nom de la catégorie ne peut pas dépasser 80 caractères")
    String name,

    @NotBlank(message = "Le slug de la catégorie est obligatoire")
    @Size(max = 90, message = "Le slug de la catégorie ne peut pas dépasser 90 caractères")
    String slug

) {}
