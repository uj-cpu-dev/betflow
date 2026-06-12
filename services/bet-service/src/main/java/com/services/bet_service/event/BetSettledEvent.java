package com.services.bet_service.event;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class BetSettledEvent {
    private UUID betId;
    private UUID userId;
    private String outcome;
    private BigDecimal payout;
    private LocalDateTime settledAt;
}
