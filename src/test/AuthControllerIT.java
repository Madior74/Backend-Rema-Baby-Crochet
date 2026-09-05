package com.ecommerce.rema_baby_crochet.auth;

import com.ecommerce.rema_baby_crochet.RemaBabyCrochetApplication;
import com.ecommerce.rema_baby_crochet.auth.dto.LoginRequest;
import com.ecommerce.rema_baby_crochet.auth.dto.LoginResponse;
import com.ecommerce.rema_baby_crochet.auth.dto.RegisterRequest;
import com.ecommerce.rema_baby_crochet.auth.dto.RegisterResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(classes = RemaBabyCrochetApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(AuthControllerIT.ProtectedEndpointTestConfiguration.class)
class AuthControllerIT {

    @Container
    static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16-alpine")
            .withDatabaseName("rema_baby_crochet_test")
            .withUsername("test")
            .withPassword("test");

    private static final String TEST_JWT_SECRET = createTestSecret();

    @Autowired
    private TestRestTemplate restTemplate;

    @DynamicPropertySource
    static void registerContainerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("jwt.secret", () -> TEST_JWT_SECRET);
        registry.add("jwt.expiration-ms", () -> "3600000");
    }

    private static String createTestSecret() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    @Test
    void registerThenLoginReturnsTokenAndTokenProtectsApi() {
        String email = "it-" + UUID.randomUUID() + "@example.com";
        String password = "StrongPassword123!";

        ResponseEntity<RegisterResponse> registration = restTemplate.postForEntity(
                "/api/auth/register",
                new RegisterRequest(email, password),
                RegisterResponse.class);

        assertThat(registration.getStatusCode().value()).isEqualTo(201);
        assertThat(registration.getBody()).isNotNull();
        assertThat(registration.getBody().email()).isEqualTo(email);
        assertThat(registration.getBody().roles()).containsExactly("ROLE_USER");

        ResponseEntity<LoginResponse> login = restTemplate.postForEntity(
                "/api/auth/login",
                new LoginRequest(email, password),
                LoginResponse.class);

        assertThat(login.getStatusCode().value()).isEqualTo(200);
        assertThat(login.getBody()).isNotNull();
        assertThat(login.getBody().token()).isNotBlank();
        assertThat(login.getBody().tokenType()).isEqualTo("Bearer");

        ResponseEntity<String> withoutToken = restTemplate.exchange(
                "/api/test/protected",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                String.class);
        assertThat(withoutToken.getStatusCode().value()).isEqualTo(401);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(login.getBody().token());
        ResponseEntity<String> withToken = restTemplate.exchange(
                "/api/test/protected",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class);

        assertThat(withToken.getStatusCode().value()).isEqualTo(200);
        assertThat(withToken.getBody()).contains("ok");
    }

    @Test
    void invalidCredentialsReturnUnauthorized() {
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/auth/login",
                new LoginRequest("[email prot\n" + //
                                        "public class AuthControllerIT {\n" + //
                                        "    \n" + //
                                        "}\n" + //
                                        "ected]", "StrongPassword123!"),
                String.class);

        assertThat(response.getStatusCode().value()).isEqualTo(401);
    }

    @TestConfiguration(proxyBeanMethods = false)
    static class ProtectedEndpointTestConfiguration {

        @Bean
        ProtectedTestController protectedTestController() {
            return new ProtectedTestController();
        }
    }

    @RestController
    @RequestMapping("/api/test")
    static class ProtectedTestController {

        @GetMapping("/protected")
        Map<String, String> protectedEndpoint() {
            return Map.of("status", "ok");
        }
    }
}
