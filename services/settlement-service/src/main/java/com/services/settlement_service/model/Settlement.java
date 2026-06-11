package com.services.settlement_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "settlements")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "bet_id", nullable = false, unique = true)
    private UUID betId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String outcome;

    @Column(nullable = false)
    private BigDecimal payout;

    @Column(name = "settled_at", nullable = false)
    private LocalDateTime settledAt;

    @PrePersist
    public void prePersist() {
        settledAt = LocalDateTime.now();
    }
}
