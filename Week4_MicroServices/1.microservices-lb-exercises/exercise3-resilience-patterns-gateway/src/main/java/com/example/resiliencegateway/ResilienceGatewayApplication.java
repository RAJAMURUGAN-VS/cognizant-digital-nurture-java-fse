package com.example.resiliencegateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Exercise 3: Resilience Patterns in an API Gateway
 *
 * Demonstrates wrapping a gateway route with a Resilience4j circuit
 * breaker, so that if the downstream "example-service" starts failing,
 * the gateway trips the circuit and fast-fails to a fallback response
 * instead of piling up latency on a dying dependency.
 */
@SpringBootApplication
public class ResilienceGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResilienceGatewayApplication.class, args);
    }
}
