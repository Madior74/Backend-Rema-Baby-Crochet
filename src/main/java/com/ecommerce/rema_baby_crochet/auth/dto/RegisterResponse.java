package com.ecommerce.rema_baby_crochet.auth.dto;

import java.util.List;

public record RegisterResponse(
        Long id,
        String email,
        List<String> roles) {
}
