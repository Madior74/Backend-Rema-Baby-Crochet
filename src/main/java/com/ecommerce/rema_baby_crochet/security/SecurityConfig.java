package com.ecommerce.rema_baby_crochet.security;

import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ecommerce.rema_baby_crochet.common.api.ApiError;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final boolean h2ConsoleEnabled;
    private final String allowedOrigins;

    public SecurityConfig(
            ObjectMapper objectMapper,
            @Value("${app.cors.allowed-origins}") String allowedOrigins,
            @Value("${app.security.h2-console-enabled:false}") boolean h2ConsoleEnabled) {
        this.objectMapper = objectMapper;
        this.h2ConsoleEnabled = h2ConsoleEnabled;
        this.allowedOrigins = allowedOrigins;
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        // Spring Security 7 constructor takes the UserDetailsService.
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider) {
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationProvider authenticationProvider,JwtFilter jwtFilter) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, exception) -> writeJsonError(response,
                                HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized", "Authentication required"))
                        .accessDeniedHandler((request, response, exception) -> writeJsonError(response,
                                HttpServletResponse.SC_FORBIDDEN, "Forbidden", "Access denied"))) 
                                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> {
                    // 1. Options et outils de dev
                    authorize.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                    authorize.requestMatchers(
                            "/v3/api-docs", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**",
                            "/actuator/health", "/actuator/info").permitAll();

                    if (h2ConsoleEnabled) {
                        authorize.requestMatchers("/h2-console/**").permitAll();
                    } else {
                        authorize.requestMatchers("/h2-console/**").denyAll();
                    }

                    authorize.requestMatchers(HttpMethod.GET, "/api/v1/products/**", "/api/v1/categories/**")
                            .permitAll();

                    authorize.requestMatchers(HttpMethod.POST, "/api/cart/**", "/api/orders/whatsapp").permitAll();
                    authorize.requestMatchers("/api/auth/login", "/api/auth/register").permitAll();

                    authorize.requestMatchers("/api/admin/**").hasRole("ADMIN");
                    authorize.requestMatchers(HttpMethod.POST, "/api/v1/products/**", "/api/v1/categories/**")
                            .hasRole("ADMIN");
                    authorize.requestMatchers(HttpMethod.PUT, "/api/v1/products/**", "/api/v1/categories/**")
                            .hasRole("ADMIN");
                    authorize.requestMatchers(HttpMethod.DELETE, "/api/v1/products/**", "/api/v1/categories/**")
                            .hasRole("ADMIN");

                    authorize.requestMatchers("/actuator/**").hasRole("ADMIN");

                    authorize.anyRequest().authenticated();
                })
                .headers(headers -> headers.frameOptions(frameOptions -> {
                    if (h2ConsoleEnabled) {
                        frameOptions.sameOrigin();
                    } else {
                        frameOptions.deny();
                    }
                }));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // on supporte plusieurs origines séparées par des virgules
        List<String> allowedOrigins = Arrays.stream(this.allowedOrigins.split(","))
                .map(String::trim)
                .toList();

        configuration.setAllowedOrigins(allowedOrigins);

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept", "X-Requested-With"));

        configuration.setAllowCredentials(true);

        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    private void writeJsonError(
            HttpServletResponse response,
            int status,
            String error,
            String message) throws IOException {
        response.setStatus(status);
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(
                new ApiError(Instant.now(), status, error, message, null, java.util.Map.of())));
    }
}
