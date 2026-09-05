package com.ecommerce.rema_baby_crochet.category;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.rema_baby_crochet.category.dto.CategoryCreateRequest;
import com.ecommerce.rema_baby_crochet.category.dto.CategoryResponse;
import com.ecommerce.rema_baby_crochet.category.dto.CategoryUpdateRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @Operation(summary = "Créer une catégorie")
    @ApiResponse(responseCode = "201", description = "Catégorie créée", content = @Content(schema = @Schema(implementation = CategoryResponse.class)))
    @ApiResponse(responseCode = "400", description = "Requête invalide")
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryCreateRequest request) {
        CategoryResponse created = categoryService.create(request);
        return ResponseEntity.created(URI.create("/api/v1/categories/" + created.id())).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une catégorie par son identifiant")
    @ApiResponse(responseCode = "200", description = "Catégorie trouvée")
    @ApiResponse(responseCode = "404", description = "Catégorie introuvable")
    public ResponseEntity<CategoryResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @GetMapping
    @Operation(summary = "Lister toutes les catégories")
    @ApiResponse(responseCode = "200", description = "Liste des catégories")
    public ResponseEntity<java.util.List<CategoryResponse>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une catégorie (remplacement complet)")
    @ApiResponse(responseCode = "200", description = "Catégorie mise à jour")
    @ApiResponse(responseCode = "404", description = "Catégorie introuvable")
    public ResponseEntity<CategoryResponse> update(
            @PathVariable Long id, @Valid @RequestBody CategoryUpdateRequest request) {
        return ResponseEntity.ok(categoryService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une catégorie")
    @ApiResponse(responseCode = "204", description = "Catégorie supprimée")
    @ApiResponse(responseCode = "404", description = "Catégorie introuvable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        categoryService.delete(id);
    }
}
