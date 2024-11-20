package com.igym.service;

import com.igym.entity.Inventory;
import com.igym.entity.InventoryTransaction;
import com.igym.entity.TransactionType;

import java.time.LocalDateTime;
import java.util.List;

public interface InventoryService {
    
    // Basic inventory operations
    Inventory getInventoryByProductId(Long productId);
    
    List<Inventory> getAllInventory();
    
    List<Inventory> getLowStockItems(Integer threshold);
    
    List<Inventory> getOutOfStockItems();
    
    // Stock operations
    void addStock(Long productId, Integer quantity, String reference, String notes, Long employeeId);
    
    void removeStock(Long productId, Integer quantity, String reference, String notes, Long employeeId);
    
    void adjustStock(Long productId, Integer newQuantity, String notes, Long employeeId);
    
    boolean hasEnoughStock(Long productId, Integer quantity);
    
    // Transaction operations
    InventoryTransaction recordTransaction(
        Long productId,
        Integer quantity,
        TransactionType type,
        String reference,
        String notes,
        Long employeeId,
        Long saleId
    );
    
    List<InventoryTransaction> getTransactionHistory(Long productId);
    
    List<InventoryTransaction> getTransactionsByDateRange(
        Long productId,
        LocalDateTime startDate,
        LocalDateTime endDate
    );
    
    // Stock movement analysis
    Integer calculateStockMovement(
        Long productId,
        LocalDateTime startDate,
        LocalDateTime endDate
    );
    
    // Stock validation
    void validateStock(Long productId, Integer quantity);
    
    // Stock updates for sales
    void processStockForSale(Long productId, Integer quantity, Long saleId, Long employeeId);
    
    void reverseStockForSale(Long saleId);
}
