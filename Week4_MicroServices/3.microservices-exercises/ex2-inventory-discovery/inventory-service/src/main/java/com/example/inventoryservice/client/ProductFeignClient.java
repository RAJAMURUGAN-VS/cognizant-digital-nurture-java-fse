package com.example.inventoryservice.client;

import com.example.inventoryservice.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Because this project has @EnableDiscoveryClient + Eureka, we reference the
 * service purely by its registered name ("product-service"). Ribbon/Spring
 * Cloud LoadBalancer resolves the actual host:port from the Eureka registry —
 * no hardcoded URL, unlike ex1's Feign client.
 */
@FeignClient(name = "product-service")
public interface ProductFeignClient {

    @GetMapping("/api/products/{id}")
    ProductDto getProduct(@PathVariable("id") Long id);

    @PatchMapping("/api/products/{id}/stock")
    ProductDto adjustProductStock(@PathVariable("id") Long id, @RequestParam("delta") int delta);
}
