package com.example.paymentservice.listener;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Subscribes to Resilience4j's CircuitBreaker event stream so every state
 * transition (CLOSED -> OPEN -> HALF_OPEN -> CLOSED), every failed call, and
 * every call rejected because the breaker is OPEN gets logged. In a real
 * deployment these logs would be shipped to your monitoring stack
 * (ELK/Prometheus/Grafana) to alert on repeated trips.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CircuitBreakerEventListener {

    private final CircuitBreakerRegistry circuitBreakerRegistry;

    @PostConstruct
    public void registerListeners() {
        circuitBreakerRegistry.getAllCircuitBreakers().forEach(this::attach);
        circuitBreakerRegistry.getEventPublisher()
                .onEntryAdded(entryAddedEvent -> attach(entryAddedEvent.getAddedEntry()));
    }

    private void attach(CircuitBreaker circuitBreaker) {
        circuitBreaker.getEventPublisher()
                .onStateTransition(event ->
                        log.warn("[CircuitBreaker:{}] state transition: {} -> {}",
                                circuitBreaker.getName(),
                                event.getStateTransition().getFromState(),
                                event.getStateTransition().getToState()))
                .onError(event ->
                        log.error("[CircuitBreaker:{}] call failed after {}ms: {}",
                                circuitBreaker.getName(),
                                event.getElapsedDuration().toMillis(),
                                event.getThrowable().toString()))
                .onSlowCallRateExceeded(event ->
                        log.warn("[CircuitBreaker:{}] slow call rate exceeded: {}%",
                                circuitBreaker.getName(), event.getSlowCallRate()))
                .onFailureRateExceeded(event ->
                        log.warn("[CircuitBreaker:{}] failure rate exceeded: {}%",
                                circuitBreaker.getName(), event.getFailureRate()))
                .onCallNotPermitted(event ->
                        log.warn("[CircuitBreaker:{}] call rejected — breaker is OPEN",
                                circuitBreaker.getName()));
    }
}
