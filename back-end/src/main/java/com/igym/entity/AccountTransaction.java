package com.igym.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "account_transactions")
@Data
@EntityListeners(AuditingEntityListener.class)
public class AccountTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "transaction_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(nullable = false)
    private Double amount;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime date;

    @Column(name = "reference_id")
    private Long referenceId; // ID of the related entity (product purchase, entry purchase, etc.)

    @Column(name = "reference_type")
    @Enumerated(EnumType.STRING)
    private ReferenceType referenceType;

    @Column
    private String description;

    public enum TransactionType {
        CREDIT,  // Money added to account
        DEBIT    // Money spent from account
    }

    public enum ReferenceType {
        PRODUCT_PURCHASE,
        ENTRY_PURCHASE,
        BALANCE_LOAD,
        REFUND
    }
}
