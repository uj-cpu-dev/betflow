package com.services.settlement_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BetSettledEvent {
    private UUID betId;
    private UUID userId;
    private String outcome;
    private BigDecimal payout;
    private LocalDateTime settledAt;
}
