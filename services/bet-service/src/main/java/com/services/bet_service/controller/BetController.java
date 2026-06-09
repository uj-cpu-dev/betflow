package com.services.bet_service.controller;

import com.services.bet_service.dto.BetResponse;
import com.services.bet_service.dto.PlaceBetRequest;
import com.services.bet_service.service.BetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bets")
@RequiredArgsConstructor
public class BetController {

    private final BetService betService;

    @PostMapping
    public ResponseEntity<BetResponse> placeBet(@Valid @RequestBody PlaceBetRequest request) {
        return ResponseEntity.ok(betService.placeBet(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BetResponse>> getBetsForUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(betService.getBetsForUser(userId));
    }
}
