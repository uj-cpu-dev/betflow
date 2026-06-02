package com.services.odds_service.service;

import com.services.odds_service.client.OddsApiClient;
import com.services.odds_service.dto.MatchDto;
import com.services.odds_service.dto.SportDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OddsService {

    private final OddsApiClient oddsApiClient;

    @Cacheable(value = "sports", key = "'all'")
    public List<SportDto> getSports() {
        return oddsApiClient.getSports();
    }

    @Cacheable(value = "matches", key = "#sport")
    public List<MatchDto> getMatches(String sport) {
        return oddsApiClient.getMatches(sport);
    }
}