package com.ecommerce.rema_baby_crochet.common.exception;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Réponse d'erreur standardisée")
public class ErrorResponse {

    @Schema(description = "Horodatage de l'erreur", example = "2025-01-10T10:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "Code HTTP de l'erreur", example = "400")
    private int status;

    @Schema(description = "Type d'erreur", example = "BAD_REQUEST")
    private String error;

    @Schema(description = "Message d'erreur", example = "Les données fournies sont invalides")
    private String message;

    @Schema(description = "Chemin de la requête", example = "/api/patient/create")
    private String path;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Détails supplémentaires de l'erreur")
    private Map<String, String> details;

    public ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}