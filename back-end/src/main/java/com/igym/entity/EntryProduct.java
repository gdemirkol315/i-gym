package com.igym.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "entry_products")
@Data
public class EntryProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Integer duration; // in days, null for one-time entries

    @Column(name = "max_entries")
    private Integer maxEntries; // null for unlimited entries

    @Column(nullable = false)
    private Double price;

    @Column(name = "entry_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EntryType entryType;

    @OneToMany(mappedBy = "entryProduct", cascade = CascadeType.ALL)
    private List<EntryProductTransaction> entryProductTransactions = new ArrayList<>();

    public enum EntryType {
        ONE_TIME,
        MULTI_PASS,
        SUBSCRIPTION
    }
}
