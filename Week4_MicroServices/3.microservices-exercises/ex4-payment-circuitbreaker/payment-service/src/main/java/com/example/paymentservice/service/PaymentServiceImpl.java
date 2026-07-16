package com.example.paymentservice.service;

import com.example.paymentservice.client.ThirdPartyPaymentClient;
import com.example.paymentservice.dto.PaymentRequest;
import com.example.paymentservice.dto.PaymentResponse;
import com.example.paymentservice.dto.ThirdPartyChargeResult;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final ThirdPartyPaymentClient thirdPartyPaymentClient;

    @Override
    // "paymentGateway" must match the instance name configured in
    // application.yml under resilience4j.circuitbreaker.instances
    @CircuitBreaker(name = "paymentGateway", fallbackMethod = "paymentFallback")
    public PaymentResponse processPayment(PaymentRequest request) {
        log.info("Charging order {} for amount {} via third-party gateway", request.getOrderId(), request.getAmount());
        ThirdPartyChargeResult result = thirdPartyPaymentClient.charge(request.getAmount());

        return PaymentResponse.builder()
                .orderId(request.getOrderId())
                .status("SUCCESS")
                .message("Payment charged successfully")
                .transactionRef(result.getTransactionRef())
                .build();
    }

    /**
     * Fallback invoked when:
     *  - the third-party call times out / errors, tripping failures past threshold, OR
     *  - the breaker is already OPEN and the call is short-circuited.
     * The extra Throwable parameter is required by Resilience4j's fallback contract.
     * Here we degrade gracefully by queuing the payment for retry instead of
     * failing the whole checkout.
     */
    public PaymentResponse paymentFallback(PaymentRequest request, Throwable throwable) {
        log.error("Fallback triggered for order {}: {}", request.getOrderId(), throwable.toString());
        return PaymentResponse.builder()
                .orderId(request.getOrderId())
                .status("FALLBACK_QUEUED")
                .message("Payment gateway is currently unavailable. Your payment has been queued and will be retried automatically.")
                .transactionRef(null)
                .build();
    }
}
