package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.dto.StockAdjustRequest;

import java.util.List;

public interface InventoryService {
    InventoryResponse getByProductId(Long productId);
    List<InventoryResponse> getAll();
    InventoryResponse adjustStock(StockAdjustRequest request);
}
