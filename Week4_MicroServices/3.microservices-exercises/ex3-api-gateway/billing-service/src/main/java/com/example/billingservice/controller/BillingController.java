package com.example.billingservice.controller;

import com.example.billingservice.entity.Invoice;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoices")
public class BillingController {

    private final List<Invoice> invoices = List.of(
            new Invoice(101L, 1L, 4599.00, "PAID"),
            new Invoice(102L, 2L, 1200.50, "PENDING"),
            new Invoice(103L, 3L, 899.00, "OVERDUE")
    );

    @GetMapping
    public List<Invoice> getAll() {
        return invoices;
    }

    @GetMapping("/{id}")
    public Invoice getById(@PathVariable Long id) {
        return invoices.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invoice not found: " + id));
    }
}
