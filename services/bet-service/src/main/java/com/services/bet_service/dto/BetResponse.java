package com.services.bet_service.dto;

import com.services.bet_service.model.Bet;
import com.services.bet_service.model.BetStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class BetResponse {
    private UUID id;
    private UUID userId;
    private String matchId;
    private String homeTeam;
    private String awayTeam;
    private String outcomeName;
    private BigDecimal odds;
    private BigDecimal stake;
    private BigDecimal potentialReturn;
    private BetStatus status;
    private LocalDateTime createdAt;

    public static BetResponse from(Bet bet) {
        return BetResponse.builder()
                .id(bet.getId())
                .userId(bet.getUserId())
                .matchId(bet.getMatchId())
                .homeTeam(bet.getHomeTeam())
                .awayTeam(bet.getAwayTeam())
                .outcomeName(bet.getOutcomeName())
                .odds(bet.getOdds())
                .stake(bet.getStake())
                .potentialReturn(bet.getPotentialReturn())
                .status(bet.getStatus())
                .createdAt(bet.getCreatedAt())
                .build();
    }
}
