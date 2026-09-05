package com.ecommerce.rema_baby_crochet.product;

import java.net.URI;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.rema_baby_crochet.product.dto.ProductCreateRequest;
import com.ecommerce.rema_baby_crochet.product.dto.ProductResponse;
import com.ecommerce.rema_baby_crochet.product.dto.ProductUpdateRequest;
import com.ecommerce.rema_baby_crochet.common.api.ApiError;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

   
    @Autowired
    private  ProductService productService;



    // @GetMapping
    // @Operation(summary="Listes les produits",description="Retourne une page de produits avec pagination, tri et filtre par nom et categorie")
    // public  List<ProductResponse> findAll()
       
    //     {
    //         return  productService.findAll();
    //     }



    @PostMapping
    @Operation(summary = "Créer un produit")
    @ApiResponse(responseCode = "201",description = "Produit crée",content = @Content(schema = @Schema(implementation = ProductResponse.class)))
    @ApiResponse(responseCode = "400", description = "Requête invalide",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductCreateRequest request){

        // affiche de des donnees recues

        ProductResponse created=productService.create(request);
        return ResponseEntity.created(URI.create("/api/v1/products"+ created.id())).body(created);
    }




    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un produit par son identifiant")
    @ApiResponse(responseCode = "200", description = "Produit trouvé")
    @ApiResponse(responseCode = "404", description = "Produit introuvable",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<ProductResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.getById(id));
    }

//     @GetMapping
//     @Operation(summary = "Rechercher des produits",
//             description = "Recherche paginée, filtrable par nom (partiel) et catégorie")
//     public ResponseEntity<Page<ProductResponse>> search(
//             @Parameter(description = "Filtre sur le nom (recherche partielle)")
//             @RequestParam(required = false) String name,
//             @Parameter(description = "Filtre par identifiant de catégorie")
//             @RequestParam(required = false) Long categoryId,
//             @Parameter(description = "Filtre par statut")
//             @RequestParam(required = false) ProductStatus status,
//             @PageableDefault(size = 20, sort = "createdAt") Pageable pageable
//     ) {
//         return ResponseEntity.ok(productService.search(name, categoryId, status, pageable));
//     }

@GetMapping
@Operation(summary = "Rechercher des produits", description = "Recherche paginée, filtrable par nom (partiel), catégorie, matière et tranche d'âge")
public ResponseEntity<Page<ProductResponse>> search(
                @Parameter(description = "Filtre sur le nom (recherche partielle)") @RequestParam(required = false) String name,

                @Parameter(description = "Filtre par identifiant de catégorie") @RequestParam(required = false) Long categoryId,

                @Parameter(description = "Filtre par statut") @RequestParam(required = false) ProductStatus status,

                @Parameter(description = "Filtre par matière") @RequestParam(required = false) String material,

                @Parameter(description = "Filtre par tranche d'âge") @RequestParam(required = false) String ageRange,

                @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(productService.search(name, categoryId, status, material, ageRange, pageable));
}

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un produit (remplacement complet)")
    @ApiResponse(responseCode = "200", description = "Produit mis à jour")
    @ApiResponse(responseCode = "404", description = "Produit introuvable",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<ProductResponse> update(
            @PathVariable UUID id, @Valid @RequestBody ProductUpdateRequest request) {
        return ResponseEntity.ok(productService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un produit")
    @ApiResponse(responseCode = "204", description = "Produit supprimé")
    @ApiResponse(responseCode = "404", description = "Produit introuvable",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        productService.delete(id);
    }



    
}
