package com.igym.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Double price;

    @Column(name = "quantity_in_stock", nullable = false)
    private Integer quantityInStock;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductConsumption> productConsumptions = new ArrayList<>();
}
