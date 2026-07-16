package com.example.resiliencegateway;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Returns a graceful, immediate response when the "exampleCircuitBreaker"
 * trips (i.e. the downstream service is failing or too slow), instead of
 * letting the caller hang or see a raw 5xx from the failing dependency.
 */
@RestController
public class FallbackController {

    @GetMapping("/fallback")
    public Mono<Map<String, Object>> fallback() {
        return Mono.just(Map.of(
                "status", HttpStatus.SERVICE_UNAVAILABLE.value(),
                "message", "The downstream service is currently unavailable. Please try again shortly."
        ));
    }
}
