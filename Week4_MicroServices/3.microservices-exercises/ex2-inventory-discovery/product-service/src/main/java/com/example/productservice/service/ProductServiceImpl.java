package com.example.productservice.service;

import com.example.productservice.dto.ProductRequest;
import com.example.productservice.dto.ProductResponse;
import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    // Pulled from Config Server via config-repo/product-service.yml
    @Value("${product.low-stock-threshold:10}")
    private int lowStockThreshold;

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .sku(request.getSku())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .build();
        return toResponse(productRepository.save(product));
    }

    @Override
    public ProductResponse getProductById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public ProductResponse adjustStock(Long id, int delta) {
        Product product = findOrThrow(id);
        int newQty = product.getStockQuantity() + delta;
        if (newQty < 0) {
            throw new IllegalArgumentException("Insufficient stock for product " + id);
        }
        product.setStockQuantity(newQty);
        return toResponse(productRepository.save(product));
    }

    private Product findOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    private ProductResponse toResponse(Product p) {
        return ProductResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .sku(p.getSku())
                .price(p.getPrice())
                .stockQuantity(p.getStockQuantity())
                .lowStock(p.getStockQuantity() <= lowStockThreshold)
                .build();
    }
}
