package com.example.authorizationserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Exercise 2 (part A): a minimal OAuth2 Authorization Server.
 *
 * Runs on port 9000 and issues JWT access tokens for the
 * "resource-server-client" registered client, which the
 * resource-server sub-project (issuer-uri: http://localhost:9000) is
 * configured to trust and validate tokens from.
 */
@SpringBootApplication
public class AuthorizationServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationServerApplication.class, args);
    }
}
