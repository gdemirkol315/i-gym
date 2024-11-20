package com.igym.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sales")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime saleDate;

    @Column(nullable = false)
    private Double subtotal;

    @Column(nullable = false)
    private Double taxRate;

    @Column(nullable = false)
    private Double taxAmount;

    @Column(nullable = false)
    private Double discountPercentage;

    @Column(nullable = false)
    private Double discountAmount;

    @Column(nullable = false)
    private Double total;

    @Column(length = 50, nullable = false)
    private String paymentMethod;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductConsumption> items = new ArrayList<>();
}
