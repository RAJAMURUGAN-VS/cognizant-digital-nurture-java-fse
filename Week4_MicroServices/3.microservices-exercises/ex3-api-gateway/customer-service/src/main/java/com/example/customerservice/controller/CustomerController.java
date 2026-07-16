package com.example.customerservice.controller;

import com.example.customerservice.entity.Customer;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final List<Customer> customers = List.of(
            new Customer(1L, "Arjun Kumar", "arjun@example.com"),
            new Customer(2L, "Priya Singh", "priya@example.com"),
            new Customer(3L, "Karthik Raj", "karthik@example.com")
    );

    @GetMapping
    public List<Customer> getAll() {
        return customers;
    }

    @GetMapping("/{id}")
    public Customer getById(@PathVariable Long id) {
        return customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Customer not found: " + id));
    }
}
