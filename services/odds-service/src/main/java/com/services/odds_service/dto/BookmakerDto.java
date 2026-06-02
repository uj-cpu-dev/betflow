package com.services.odds_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookmakerDto {
    private String key;
    private String title;
    private List<MarketDto> markets;
}
