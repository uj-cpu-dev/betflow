package com.services.odds_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OddsPollingService {

    private final OddsService oddsService;

    private static final List<String> ACTIVE_SPORTS = List.of(
            "tennis_atp_french_open",
            "tennis_wta_french_open"
    );

    @Scheduled(fixedRateString = "${odds.poll.interval-ms:300000}") // 5 min default
    public void pollOdds() {
        log.info("Polling odds for {} sports", ACTIVE_SPORTS.size());
        ACTIVE_SPORTS.forEach(sport -> {
            try {
                oddsService.getMatches(sport);
                log.info("Refreshed odds for {}", sport);
            } catch (Exception e) {
                log.warn("Failed to refresh odds for {}: {}", sport, e.getMessage());
            }
        });
    }
}