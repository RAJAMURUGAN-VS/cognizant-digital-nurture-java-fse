package com.example.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {
    private Long orderId;
    private String status;       // SUCCESS, FALLBACK_QUEUED, FAILED
    private String message;
    private String transactionRef;
}
