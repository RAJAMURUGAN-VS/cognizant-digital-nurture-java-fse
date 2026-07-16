package com.example.jwtauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Exercise 3: Using JSON Web Tokens (JWT) for Secure Communication
 *
 * Hand-rolled JWT authentication: POST /auth/login with a
 * username/password issues a signed JWT (JwtTokenProvider), which
 * must then be sent as "Authorization: Bearer <token>" on subsequent
 * requests. JwtTokenFilter validates that header on every request and
 * populates the SecurityContext accordingly.
 */
@SpringBootApplication
public class JwtAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtAuthApplication.class, args);
    }
}
