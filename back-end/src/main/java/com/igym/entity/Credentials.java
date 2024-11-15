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

    @Column(nullable = false, unique = true)
    private String username;

    @Column(name = "hashed_pass", nullable = false)
    private String hashedPassword;

    @Override
    public String toString() {
        return "Credentials{" +
                "employeeId=" + employeeId +
                ", username='" + username + '\'' +
                '}';
    }
}
