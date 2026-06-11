package com.services.settlement_service.event;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class BetPlacedEvent {
    private UUID betId;
    private UUID userId;
    private String matchId;
    private String homeTeam;
    private String awayTeam;
    private String outcomeName;
    private BigDecimal odds;
    private BigDecimal stake;
    private BigDecimal potentialReturn;
    private LocalDateTime placedAt;
}
