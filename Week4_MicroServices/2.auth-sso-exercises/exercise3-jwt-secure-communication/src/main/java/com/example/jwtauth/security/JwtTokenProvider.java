package com.example.jwtauth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

/**
 * Creates and validates JWTs using the current jjwt 0.12.x builder API
 * (Jwts.builder().subject(...).signWith(secretKey)), replacing the
 * exercise sheet's deprecated
 * Jwts.builder().setClaims(...).signWith(SignatureAlgorithm.HS256, rawSecretString)
 * call.
 */
@Component
public class JwtTokenProvider {

    private static final long VALIDITY_MS = 3_600_000; // 1 hour

    private final SecretKey signingKey;

    @Autowired
    public JwtTokenProvider(SecretKey jwtSigningKey) {
        this.signingKey = jwtSigningKey;
    }

    public String createToken(String username) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + VALIDITY_MS);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(validity)
                .signWith(signingKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String username = claims.getSubject();

        return new UsernamePasswordAuthenticationToken(
                username,
                token,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
