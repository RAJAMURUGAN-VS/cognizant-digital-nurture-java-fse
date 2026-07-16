package com.example.oauth2login;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Exercise 1: Implementing Centralized Authentication with OAuth 2.1/OIDC
 *
 * A minimal app that delegates login to an external OIDC provider
 * (Google, by default, per application.yml) using Spring Security's
 * oauth2Login(). Once logged in, GET /user returns the authenticated
 * principal's claims.
 */
@SpringBootApplication
public class OAuth2LoginApplication {

    public static void main(String[] args) {
        SpringApplication.run(OAuth2LoginApplication.class, args);
    }
}
