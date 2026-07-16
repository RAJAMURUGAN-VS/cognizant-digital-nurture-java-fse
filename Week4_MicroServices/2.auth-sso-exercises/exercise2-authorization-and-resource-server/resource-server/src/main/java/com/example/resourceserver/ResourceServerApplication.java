package com.example.resourceserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Exercise 2 (part B): Resource Server
 *
 * Validates JWT access tokens against the issuer configured in
 * application.yml (by default, the authorization-server sub-project
 * running alongside it) and exposes a protected endpoint.
 */
@SpringBootApplication
public class ResourceServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResourceServerApplication.class, args);
    }
}
