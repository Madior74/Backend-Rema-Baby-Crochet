package com.ecommerce.rema_baby_crochet.auth;


import com.ecommerce.rema_baby_crochet.auth.dto.LoginRequest;
import com.ecommerce.rema_baby_crochet.auth.dto.LoginResponse;
import com.ecommerce.rema_baby_crochet.auth.dto.RegisterRequest;
import com.ecommerce.rema_baby_crochet.auth.dto.RegisterResponse;
import com.ecommerce.rema_baby_crochet.security.JwtUtils;
import com.ecommerce.rema_baby_crochet.user.Role;
import com.ecommerce.rema_baby_crochet.user.User;
import com.ecommerce.rema_baby_crochet.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        //donnees
        log.info("--------------------------------------------------------------------------------------------");
        log.info("RegisterRequest: {}", request);
        try {
            User user = userService.createUser(request);
            RegisterResponse response = new RegisterResponse(
                    user.getId(),
                    user.getEmail(),
                    List.of(Role.ROLE_CLIENT));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error during registration: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


  

@PostMapping("/login")
public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
    String normalizedEmail = request.email().trim().toLowerCase(Locale.ROOT);
    try {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(normalizedEmail, request.password()));
        
        // Dynamically extract roles from the authenticated user
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String token = jwtUtils.generateToken(authentication);
        return ResponseEntity.ok(new LoginResponse(
                token,
                "Bearer",
                jwtUtils.getExpirationMs() / 1000,
                normalizedEmail,
                roles // Use dynamic roles here
        ));
    } catch (AuthenticationException exception) {
        log.warn("Authentication failed for email={}", normalizedEmail);
        throw new com.ecommerce.rema_baby_crochet.common.exception.InvalidCredentialsException();
    }
}
}
