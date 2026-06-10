package com.services.bet_service.service;

import com.services.bet_service.dto.BetResponse;
import com.services.bet_service.dto.PlaceBetRequest;
import com.services.bet_service.event.BetPlacedEvent;
import com.services.bet_service.model.Bet;
import com.services.bet_service.repository.BetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BetService {

    private final BetRepository betRepository;

    private final KafkaProducerService kafkaProducerService;

    public BetResponse placeBet(PlaceBetRequest request) {
        BigDecimal potentialReturn = request.getStake()
                .multiply(request.getOdds())
                .setScale(2, RoundingMode.HALF_UP);

        Bet bet = Bet.builder()
                .userId(request.getUserId())
                .matchId(request.getMatchId())
                .homeTeam(request.getHomeTeam())
                .awayTeam(request.getAwayTeam())
                .outcomeName(request.getOutcomeName())
                .odds(request.getOdds())
                .stake(request.getStake())
                .potentialReturn(potentialReturn)
                .build();

        Bet saved = betRepository.save(bet);
        log.info("Bet placed: {} for user {}", saved.getId(), saved.getUserId());

        kafkaProducerService.publishBetPlaced(BetPlacedEvent.builder()
                .betId(saved.getId())
                .userId(saved.getUserId())
                .matchId(saved.getMatchId())
                .homeTeam(saved.getHomeTeam())
                .awayTeam(saved.getAwayTeam())
                .outcomeName(saved.getOutcomeName())
                .odds(saved.getOdds())
                .stake(saved.getStake())
                .potentialReturn(saved.getPotentialReturn())
                .placedAt(saved.getCreatedAt())
                .build());

        return BetResponse.from(saved);
    }

    public List<BetResponse> getBetsForUser(UUID userId) {
        return betRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(BetResponse::from)
                .toList();
    }
}
