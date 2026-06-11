package com.services.settlement_service.service;

import com.services.settlement_service.event.BetPlacedEvent;
import com.services.settlement_service.event.BetSettledEvent;
import com.services.settlement_service.model.Settlement;
import com.services.settlement_service.repository.SettlementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class SettlementService {

    private final SettlementRepository settlementRepository;
    private final KafkaTemplate<String, BetSettledEvent> kafkaTemplate;
    private final Random random = new Random();

    public void settle(BetPlacedEvent event) {
        // Idempotency check — never settle the same bet twice
        if (settlementRepository.existsByBetId(event.getBetId())) {
            log.warn("Bet {} already settled, skipping", event.getBetId());
            return;
        }

        // Randomly determine outcome (WIN/LOSS) — real logic comes later
        boolean win = random.nextBoolean();
        String outcome = win ? "WIN" : "LOSS";
        BigDecimal payout = win ? event.getPotentialReturn() : BigDecimal.ZERO;

        Settlement settlement = Settlement.builder()
                .betId(event.getBetId())
                .userId(event.getUserId())
                .outcome(outcome)
                .payout(payout)
                .build();

        settlementRepository.save(settlement);
        log.info("Settled bet {} for user {} — {} payout: £{}",
                event.getBetId(), event.getUserId(), outcome, payout);

        // Publish bet.settled event
        BetSettledEvent settledEvent = BetSettledEvent.builder()
                .betId(event.getBetId())
                .userId(event.getUserId())
                .outcome(outcome)
                .payout(payout)
                .settledAt(settlement.getSettledAt())
                .build();

        kafkaTemplate.send("bet.settled", event.getUserId().toString(), settledEvent)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish bet.settled for betId {}: {}",
                                event.getBetId(), ex.getMessage());
                    } else {
                        log.info("Published bet.settled for betId {} to partition {}",
                                event.getBetId(),
                                result.getRecordMetadata().partition());
                    }
                });
    }
}
