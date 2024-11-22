package com.igym.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "entry_product_transaction")
@Data
@EntityListeners(AuditingEntityListener.class)
public class EntryProductTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entry_product_id", nullable = false)
    private EntryProduct entryProduct;

    @CreatedDate
    @Column(name = "date", nullable = false, updatable = false)
    private LocalDateTime purchaseDate;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @Column(name = "number_of_entries")
    private Integer noEntries;

    @Column(name = "is_active")
    private boolean isActive = true;

    @OneToMany(mappedBy = "entryProductTransaction", cascade = CascadeType.ALL)
    private List<EntryConsumption> entryConsumptions = new ArrayList<>();
}
