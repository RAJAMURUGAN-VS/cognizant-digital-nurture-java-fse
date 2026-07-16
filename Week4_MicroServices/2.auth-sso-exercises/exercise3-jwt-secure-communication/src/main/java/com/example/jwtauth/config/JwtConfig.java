package com.example.jwtauth.config;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

/**
 * NOTE ON THE ORIGINAL EXERCISE SHEET: the sheet's JwtConfig just exposes
 * the raw secret string (spring.security.jwt.secret) and JwtTokenProvider
 * passes that raw string straight into
 * Jwts.builder().signWith(SignatureAlgorithm.HS256, secretString) — an API
 * that's deprecated in modern jjwt and unsafe if the string is shorter
 * than the required 256 bits for HS256. Here, the configured secret is
 * expected to be a Base64-encoded key of sufficient length, and it's
 * decoded once into a real javax.crypto.SecretKey via Keys.hmacShaKeyFor,
 * which JwtTokenProvider then signs/verifies with directly.
 *
 * Generate a suitable secret with, e.g.:
 *   openssl rand -base64 32
 */
@Configuration
public class JwtConfig {

    @Value("${spring.security.jwt.secret}")
    private String secret;

    @Bean
    public SecretKey jwtSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
