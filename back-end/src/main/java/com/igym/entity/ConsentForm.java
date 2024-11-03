package com.igym.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "consent_forms")
@Data
@EntityListeners(AuditingEntityListener.class)
public class ConsentForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "consent_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ConsentType consentType;

    @CreatedDate
    @Column(name = "date_signed", nullable = false, updatable = false)
    private LocalDateTime dateSigned;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    public enum ConsentType {
        TERMS_AND_CONDITIONS,
        PRIVACY_POLICY,
        LIABILITY_WAIVER,
        HEALTH_DECLARATION
    }
}
