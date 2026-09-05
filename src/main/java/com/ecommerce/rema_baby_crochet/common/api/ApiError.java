package com.ecommerce.rema_baby_crochet.common.api;


import java.time.Instant;
import java.util.Map;

public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> details) {
}
