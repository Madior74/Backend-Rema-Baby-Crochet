package com.ecommerce.rema_baby_crochet.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
      @Bean
    public OpenAPI productServiceOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Product Service API")
                .version("v1")
                .description("API REST de gestion du catalogue produits e-commerce")
                .contact(new Contact().name("Équipe Plateforme").email("platform@ecommerce.example")));
    }
    
}
