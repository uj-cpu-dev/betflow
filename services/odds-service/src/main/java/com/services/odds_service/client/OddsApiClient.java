package com.services.odds_service.client;

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

    public List<SportDto> getSports() {
        return restClient.get()
                .uri(baseUrl + "/sports?apiKey=" + apiKey)
                .retrieve()
                .body(new ParameterizedTypeReference<List<SportDto>>() {});
    }
}
