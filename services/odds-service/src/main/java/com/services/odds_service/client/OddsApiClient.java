package com.services.odds_service.client;

import com.services.odds_service.dto.MatchDto;
import com.services.odds_service.dto.SportDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OddsApiClient {

    private final RestClient restClient;

    @Value("${odds.api.key}")
    private String apiKey;

    @Value("${odds.api.base-url}")
    private String baseUrl;

    @jakarta.annotation.PostConstruct
    public void init() {
        System.out.println("=== API KEY LOADED: [" + apiKey + "] ===");
        System.out.println("=== BASE URL LOADED: [" + baseUrl + "] ===");
    }

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
}
