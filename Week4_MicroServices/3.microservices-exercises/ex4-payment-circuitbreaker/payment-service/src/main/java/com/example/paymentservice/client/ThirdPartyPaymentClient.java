package com.example.paymentservice.client;

import com.example.paymentservice.dto.ThirdPartyChargeResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class ThirdPartyPaymentClient {

    private final WebClient thirdPartyWebClient;

    public ThirdPartyChargeResult charge(double amount) {
        // .block() at the edge — this call is what the Circuit Breaker wraps
        return thirdPartyWebClient.post()
                .uri(uriBuilder -> uriBuilder.path("/third-party/charge")
                        .queryParam("amount", amount)
                        .build())
                .retrieve()
                .bodyToMono(ThirdPartyChargeResult.class)
                .block();
    }
}
