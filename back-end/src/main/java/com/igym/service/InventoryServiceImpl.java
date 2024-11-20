package com.igym.service;

import com.igym.entity.*;
import com.igym.repository.EmployeeRepository;
import com.igym.repository.InventoryRepository;
import com.igym.repository.InventoryTransactionRepository;
import com.igym.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryTransactionRepository transactionRepository;
    private final ProductRepository productRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional(readOnly = true)
    public Inventory getInventoryByProductId(Long productId) {
        return inventoryRepository.findByProductId(productId)
            .orElseThrow(() -> new EntityNotFoundException("Inventory not found for product: " + productId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getLowStockItems(Integer threshold) {
        return inventoryRepository.findLowStockItems(threshold);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getOutOfStockItems() {
        return inventoryRepository.findOutOfStockItems();
    }

    @Override
    @Transactional
    @Retryable(value = OptimisticLockException.class, maxAttempts = 3)
    public void addStock(Long productId, Integer quantity, String reference, String notes, Long employeeId) {
        Inventory inventory = getInventoryWithLock(productId);
        Employee employee = getEmployee(employeeId);

        inventory.updateStock(quantity, TransactionType.STOCK_IN);
        
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setInventory(inventory);
        transaction.setQuantity(quantity);
        transaction.setType(TransactionType.STOCK_IN);
        transaction.setReference(reference);
        transaction.setNotes(notes);
        transaction.setEmployee(employee);
        transaction.setTimestamp(LocalDateTime.now());

        inventory.addTransaction(transaction);
        inventoryRepository.save(inventory);
    }

    @Override
    @Transactional
    @Retryable(value = OptimisticLockException.class, maxAttempts = 3)
    public void removeStock(Long productId, Integer quantity, String reference, String notes, Long employeeId) {
        Inventory inventory = getInventoryWithLock(productId);
        Employee employee = getEmployee(employeeId);

        validateStock(productId, quantity);
        inventory.updateStock(quantity, TransactionType.STOCK_OUT);
        
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setInventory(inventory);
        transaction.setQuantity(quantity);
        transaction.setType(TransactionType.STOCK_OUT);
        transaction.setReference(reference);
        transaction.setNotes(notes);
        transaction.setEmployee(employee);
        transaction.setTimestamp(LocalDateTime.now());

        inventory.addTransaction(transaction);
        inventoryRepository.save(inventory);
    }

    @Override
    @Transactional
    public void adjustStock(Long productId, Integer newQuantity, String notes, Long employeeId) {
        Inventory inventory = getInventoryWithLock(productId);
        Employee employee = getEmployee(employeeId);

        int difference = newQuantity - inventory.getQuantityInStock();
        TransactionType type = difference >= 0 ? TransactionType.STOCK_IN : TransactionType.STOCK_OUT;
        
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setInventory(inventory);
        transaction.setQuantity(Math.abs(difference));
        transaction.setType(type);
        transaction.setReference("ADJUSTMENT");
        transaction.setNotes(notes);
        transaction.setEmployee(employee);
        transaction.setTimestamp(LocalDateTime.now());

        inventory.setQuantityInStock(newQuantity);
        inventory.addTransaction(transaction);
        inventoryRepository.save(inventory);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasEnoughStock(Long productId, Integer quantity) {
        return inventoryRepository.hasEnoughStock(productId, quantity);
    }

    @Override
    @Transactional
    public InventoryTransaction recordTransaction(Long productId, Integer quantity, TransactionType type,
                                                String reference, String notes, Long employeeId, Long saleId) {
        Inventory inventory = getInventoryWithLock(productId);
        Employee employee = getEmployee(employeeId);

        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setInventory(inventory);
        transaction.setQuantity(quantity);
        transaction.setType(type);
        transaction.setReference(reference);
        transaction.setNotes(notes);
        transaction.setEmployee(employee);
        transaction.setTimestamp(LocalDateTime.now());
        
        if (saleId != null) {
            // Assuming you have a method to get Sale entity
            // transaction.setSale(getSale(saleId));
        }

        inventory.addTransaction(transaction);
        return transactionRepository.save(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryTransaction> getTransactionHistory(Long productId) {
        Inventory inventory = getInventoryByProductId(productId);
        return transactionRepository.findByInventoryId(inventory.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryTransaction> getTransactionsByDateRange(Long productId, LocalDateTime startDate, LocalDateTime endDate) {
        Inventory inventory = getInventoryByProductId(productId);
        return transactionRepository.findTransactionsByDateRange(inventory.getId(), startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer calculateStockMovement(Long productId, LocalDateTime startDate, LocalDateTime endDate) {
        Inventory inventory = getInventoryByProductId(productId);
        return transactionRepository.calculateStockMovement(inventory.getId(), startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public void validateStock(Long productId, Integer quantity) {
        if (!hasEnoughStock(productId, quantity)) {
            throw new IllegalStateException("Insufficient stock for product: " + productId);
        }
    }

    @Override
    @Transactional
    @Retryable(value = OptimisticLockException.class, maxAttempts = 3)
    public void processStockForSale(Long productId, Integer quantity, Long saleId, Long employeeId) {
        removeStock(productId, quantity, "SALE-" + saleId, "Stock reduction for sale", employeeId);
    }

    @Override
    @Transactional
    @Retryable(value = OptimisticLockException.class, maxAttempts = 3)
    public void reverseStockForSale(Long saleId) {
        List<InventoryTransaction> saleTransactions = transactionRepository.findBySaleId(saleId);
        for (InventoryTransaction transaction : saleTransactions) {
            Inventory inventory = transaction.getInventory();
            inventory.updateStock(transaction.getQuantity(), TransactionType.STOCK_IN);
            inventoryRepository.save(inventory);
            
            InventoryTransaction reversal = new InventoryTransaction();
            reversal.setInventory(inventory);
            reversal.setQuantity(transaction.getQuantity());
            reversal.setType(TransactionType.STOCK_IN);
            reversal.setReference("SALE-REVERSAL-" + saleId);
            reversal.setNotes("Reversal of sale transaction");
            reversal.setTimestamp(LocalDateTime.now());
            
            transactionRepository.save(reversal);
        }
    }

    private Inventory getInventoryWithLock(Long productId) {
        return inventoryRepository.findByProductIdWithLock(productId)
            .orElseThrow(() -> new EntityNotFoundException("Inventory not found for product: " + productId));
    }

    private Employee getEmployee(Long employeeId) {
        return employeeRepository.findById(employeeId)
            .orElseThrow(() -> new EntityNotFoundException("Employee not found: " + employeeId));
    }
}
