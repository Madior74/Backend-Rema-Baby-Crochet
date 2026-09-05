package com.ecommerce.rema_baby_crochet.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
    // Clé secrète pour signer le JWT (à mettre dans application.properties)
    @Value("${jwt.expiration-ms}")
    private Long expiration;

    @Value("${jwt.secret}")
    private String secret;

    // Générer la clé secrète
   private SecretKey getSigningKey() {
       return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
   }

    // Générer un token JWT
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return generateToken(userDetails);
    }

    // Créer le token avec les claims
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extraire le username du token
    public Optional<String> extractUsername(String token) {
        return Optional.ofNullable(extractClaim(token, Claims::getSubject));
    }

    // Extraire la date d'expiration
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extraire un claim spécifique
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extraire tous les claims
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey()) // Use verifyWith for HMAC
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Vérifier si le token est expiré
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Valider le token
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token).orElse(null);
        return (username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    //getExpirationMs
    public Long getExpirationMs() {
        return expiration;
    }

}
