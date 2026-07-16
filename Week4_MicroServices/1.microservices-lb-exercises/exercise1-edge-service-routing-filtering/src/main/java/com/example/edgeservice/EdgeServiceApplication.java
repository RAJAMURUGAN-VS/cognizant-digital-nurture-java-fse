package com.example.edgeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Exercise 1: Implementing Edge Services for Routing and Filtering
 *
 * Entry point for the Spring Cloud Gateway edge service. The gateway
 * routes incoming requests according to the rules defined in
 * application.properties, and every request passes through the
 * custom LoggingFilter defined in this project.
 */
@SpringBootApplication
public class EdgeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EdgeServiceApplication.class, args);
    }
}
