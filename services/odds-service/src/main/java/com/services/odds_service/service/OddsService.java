package com.services.odds_service.service;

import com.services.odds_service.client.OddsApiClient;
import com.services.odds_service.dto.SportDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OddsService {

    private final OddsApiClient oddsApiClient;

    public List<SportDto> getSports() {
        return oddsApiClient.getSports();
    }
}