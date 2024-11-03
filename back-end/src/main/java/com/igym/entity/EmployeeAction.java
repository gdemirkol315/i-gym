package com.igym.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "employee_actions")
@Data
@EntityListeners(AuditingEntityListener.class)
public class EmployeeAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "action_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    @Column(name = "target_id")
    private Long targetId;

    @CreatedDate
    @Column(name = "date_time", nullable = false, updatable = false)
    private LocalDateTime dateTime;

    @Column(name = "description")
    private String description;

    public enum ActionType {
        CUSTOMER_REGISTRATION,
        ENTRY_CHECK,
        PRODUCT_SALE,
        SUBSCRIPTION_PURCHASE,
        BALANCE_UPDATE,
        INVENTORY_UPDATE,
        PRICE_UPDATE,
        EMPLOYEE_CREATION
    }
}
