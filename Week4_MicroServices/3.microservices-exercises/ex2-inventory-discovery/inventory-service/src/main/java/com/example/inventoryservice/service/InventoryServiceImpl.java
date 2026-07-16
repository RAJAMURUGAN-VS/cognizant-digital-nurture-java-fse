package com.example.inventoryservice.service;

import com.example.inventoryservice.client.ProductFeignClient;
import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.dto.ProductDto;
import com.example.inventoryservice.dto.StockAdjustRequest;
import com.example.inventoryservice.entity.InventoryItem;
import com.example.inventoryservice.repository.InventoryItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryItemRepository inventoryItemRepository;
    private final ProductFeignClient productFeignClient;

    // Pulled from Config Server via config-repo/inventory-service.yml
    @Value("${inventory.warehouse-code:WH-DEFAULT}")
    private String defaultWarehouseCode;

    @Override
    public InventoryResponse getByProductId(Long productId) {
        InventoryItem item = inventoryItemRepository.findByProductId(productId)
                .orElseThrow(() -> new EntityNotFoundException("No inventory record for product " + productId));
        ProductDto product = productFeignClient.getProduct(productId);
        return toResponse(item, product);
    }

    @Override
    public List<InventoryResponse> getAll() {
        return inventoryItemRepository.findAll().stream()
                .map(item -> {
                    ProductDto product = safeGetProduct(item.getProductId());
                    return toResponse(item, product);
                })
                .toList();
    }

    @Override
    public InventoryResponse adjustStock(StockAdjustRequest request) {
        InventoryItem item = inventoryItemRepository.findByProductId(request.getProductId())
                .orElseGet(() -> InventoryItem.builder()
                        .productId(request.getProductId())
                        .quantityOnHand(0)
                        .reservedQuantity(0)
                        .warehouseCode(defaultWarehouseCode)
                        .build());

        int newQty = item.getQuantityOnHand() + request.getDelta();
        if (newQty < 0) {
            throw new IllegalArgumentException("Stock cannot go negative for product " + request.getProductId());
        }
        item.setQuantityOnHand(newQty);
        InventoryItem saved = inventoryItemRepository.save(item);

        // Keep Product Service's stock figure in sync (cross-service call via Feign + Eureka)
        ProductDto product = productFeignClient.adjustProductStock(request.getProductId(), request.getDelta());

        return toResponse(saved, product);
    }

    private ProductDto safeGetProduct(Long productId) {
        try {
            return productFeignClient.getProduct(productId);
        } catch (Exception e) {
            return null;
        }
    }

    private InventoryResponse toResponse(InventoryItem item, ProductDto product) {
        return InventoryResponse.builder()
                .productId(item.getProductId())
                .productName(product != null ? product.getName() : "Unknown")
                .quantityOnHand(item.getQuantityOnHand())
                .reservedQuantity(item.getReservedQuantity())
                .availableQuantity(item.getQuantityOnHand() - item.getReservedQuantity())
                .warehouseCode(item.getWarehouseCode())
                .build();
    }
}
