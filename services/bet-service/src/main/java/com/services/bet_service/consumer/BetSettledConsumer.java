package com.services.bet_service.consumer;

import com.services.bet_service.event.BetSettledEvent;
import com.services.bet_service.model.Bet;
import com.services.bet_service.model.BetStatus;
import com.services.bet_service.repository.BetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BetSettledConsumer {

    private final BetRepository betRepository;

    @KafkaListener(
            topics = "bet.settled",
            groupId = "bet-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(BetSettledEvent event) {
        log.info("Received bet.settled event for betId: {}", event.getBetId());

        betRepository.findById(event.getBetId()).ifPresentOrElse(bet -> {
            bet.setStatus(BetStatus.valueOf(event.getOutcome().equals("WIN") ? "WON" : "LOST"));
            bet.setPayout(event.getPayout());
            betRepository.save(bet);
            log.info("Updated bet {} status to {}", bet.getId(), bet.getStatus());
        }, () -> log.warn("Bet {} not found", event.getBetId()));
    }
}
