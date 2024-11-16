package com.igym.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "credentials")
@Getter
@Setter
public class Credentials {
    @Id
    private Long employeeId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "hashed_pass", nullable = false)
    private String hashedPassword;

    @Override
    public String toString() {
        return "Credentials{" +
                "employeeId=" + employeeId +
                ", email='" + (employee != null ? employee.getEmail() : null) + '\'' +
                '}';
    }
}
