package com.example.paymentservice.controller;

import com.example.paymentservice.dto.ThirdPartyChargeResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class ThirdPartyGatewaySimulatorController {

    /**
     * Simulates a slow / unreliable third-party payment gateway:
     *  - ~40% of calls sleep 4-6s (well past our 2s timeout)
     *  - ~20% of calls throw an error outright
     *  - remaining calls succeed quickly
     * This gives PaymentService's Circuit Breaker real timeouts and failures
     * to trip on, instead of a hardcoded "always slow" stub.
     */
    @PostMapping("/third-party/charge")
    public ThirdPartyChargeResult charge(@RequestParam double amount) throws InterruptedException {
        double roll = ThreadLocalRandom.current().nextDouble();

        if (roll < 0.4) {
            Thread.sleep(4000 + ThreadLocalRandom.current().nextInt(2000)); // 4-6s: too slow
        } else if (roll < 0.6) {
            throw new RuntimeException("Third-party gateway returned 500");
        }

        return new ThirdPartyChargeResult(UUID.randomUUID().toString(), "CHARGED");
    }
}
