package com.igym.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "inventory")
@Data
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Version  // Optimistic locking
    private Long version;
    
    @Column(name = "quantity_in_stock", nullable = false)
    private Integer quantityInStock;
    
    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InventoryTransaction> transactions = new ArrayList<>();

    // Helper method to add transaction
    public void addTransaction(InventoryTransaction transaction) {
        transactions.add(transaction);
        transaction.setInventory(this);
    }

    // Helper method to remove transaction
    public void removeTransaction(InventoryTransaction transaction) {
        transactions.remove(transaction);
        transaction.setInventory(null);
    }

    // Helper method to update stock
    public void updateStock(Integer quantity, TransactionType type) {
        if (type == TransactionType.STOCK_IN) {
            this.quantityInStock += quantity;
        } else if (type == TransactionType.STOCK_OUT) {
            if (this.quantityInStock < quantity) {
                throw new IllegalStateException("Insufficient stock");
            }
            this.quantityInStock -= quantity;
        }
    }
}
