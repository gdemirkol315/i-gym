package com.igym.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "salt")
@Data
public class Salt {
    @Id
    private Long employeeId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(nullable = false)
    private String salt;
}
