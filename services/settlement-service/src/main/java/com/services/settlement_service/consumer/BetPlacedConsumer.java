package com.services.settlement_service.consumer;

import com.services.settlement_service.event.BetPlacedEvent;
import com.services.settlement_service.service.SettlementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BetPlacedConsumer {

    private final SettlementService settlementService;

    @KafkaListener(
            topics = "bet.placed",
            groupId = "settlement-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(BetPlacedEvent event) {
        log.info("Received bet.placed event for betId: {}", event.getBetId());
        settlementService.settle(event);
    }
}
