package com.services.odds_service.controller;

import com.services.odds_service.dto.MatchDto;
import com.services.odds_service.dto.SportDto;
import com.services.odds_service.service.OddsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/odds")
@RequiredArgsConstructor
public class OddsController {

    private final OddsService oddsService;

    @GetMapping("/sports")
    public ResponseEntity<List<SportDto>> getSports() {
        return ResponseEntity.ok(oddsService.getSports());
    }

    @GetMapping("/matches/{sport}")
    public ResponseEntity<List<MatchDto>> getMatches(@PathVariable String sport) {
        return ResponseEntity.ok(oddsService.getMatches(sport));
    }
}
