package com.igym.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Entity
@Table(name = "entry_consumption")
@Data
@EntityListeners(AuditingEntityListener.class)
public class EntryConsumption {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entry_product_transaction", nullable = false)
    private EntryProductTransaction entryProductTransaction;

    @CreatedDate
    @Column(name = "date", nullable = false, updatable = false)
    private LocalDateTime consumptionDate;

}
