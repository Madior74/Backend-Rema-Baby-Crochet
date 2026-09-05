package com.ecommerce.rema_baby_crochet.common.exception;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Réponse d'erreur pour les erreurs de validation")
public class ValidationErrorResponse extends ErrorResponse {

    @Schema(description = "Erreurs de validation par champ")
    private Map<String, String> fieldErrors;

    public ValidationErrorResponse(LocalDateTime timestamp, int status, String error,
                                   String message, String path, Map<String, String> fieldErrors) {
        super(timestamp, status, error, message, path);
        this.fieldErrors = fieldErrors;
    }
}