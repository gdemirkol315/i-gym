package com.igym.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "salt")
@Getter
@Setter
public class Salt {
    @Id
    private Long employeeId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(nullable = false)
    private String salt;

    @Override
    public String toString() {
        return "Salt{" +
                "employeeId=" + employeeId +
                '}';
    }
}
