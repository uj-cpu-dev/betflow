package com.services.odds_service.client;

import com.services.odds_service.dto.MatchDto;
import com.services.odds_service.dto.SportDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OddsApiClient {

    private final RestClient restClient;

    @Value("${odds.api.key}")
    private String apiKey;

    @Value("${odds.api.base-url}")
    private String baseUrl;

    public List<SportDto> getSports() {
        return restClient.get()
                .uri(baseUrl + "/sports?apiKey=" + apiKey)
                .retrieve()
                .body(new ParameterizedTypeReference<List<SportDto>>() {});
    }

    public List<MatchDto> getMatches(String sport) {
        return restClient.get()
                .uri(baseUrl + "/sports/" + sport + "/odds"
                        + "?apiKey=" + apiKey
                        + "&regions=uk"
                        + "&markets=h2h"
                        + "&oddsFormat=decimal")
                .retrieve()
                .body(new ParameterizedTypeReference<List<MatchDto>>() {});
    }

    public List<MatchDto> getMatchesFallback(String sport, Exception e) {
        log.warn("Circuit breaker open for sport {}: {}", sport, e.getMessage());
        return List.of(); // return empty list, cache still serves last good data
    }
}
