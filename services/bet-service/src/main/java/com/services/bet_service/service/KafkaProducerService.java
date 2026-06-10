package com.services.bet_service.service;

import com.services.bet_service.event.BetPlacedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private static final String TOPIC = "bet.placed";
    private final KafkaTemplate<String, BetPlacedEvent> kafkaTemplate;

    public void publishBetPlaced(BetPlacedEvent event) {
        kafkaTemplate.send(TOPIC, event.getUserId().toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish bet.placed for betId {}: {}",
                                event.getBetId(), ex.getMessage());
                    } else {
                        log.info("Published bet.placed for betId {} to partition {}",
                                event.getBetId(),
                                result.getRecordMetadata().partition());
                    }
                });
    }
}

