package com.services.bet_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PlaceBetRequest {

    @NotNull
    private UUID userId;

    @NotBlank
    private String matchId;

    @NotBlank
    private String homeTeam;

    @NotBlank
    private String awayTeam;

    @NotBlank
    private String outcomeName;

    @NotNull
    @DecimalMin("1.01")
    private BigDecimal odds;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal stake;
}
