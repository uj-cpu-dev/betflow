package com.services.odds_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class MatchDto {
    private String id;
    private String sport_key;
    private String sport_title;

    @JsonProperty("commence_time")
    private String commenceTime;

    @JsonProperty("home_team")
    private String homeTeam;

    @JsonProperty("away_team")
    private String awayTeam;

    private List<BookmakerDto> bookmakers;
}
