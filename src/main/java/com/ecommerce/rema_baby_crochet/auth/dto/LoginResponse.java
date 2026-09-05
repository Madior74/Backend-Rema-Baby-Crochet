package com.ecommerce.rema_baby_crochet.auth.dto;

import java.util.List;

public record LoginResponse(
        String token,
        String tokenType,
        long expiresIn,
        String email,
        List<String> roles
) {
}
