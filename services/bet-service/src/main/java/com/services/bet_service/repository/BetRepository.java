package com.services.bet_service.repository;

import com.services.bet_service.model.Bet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BetRepository extends JpaRepository<Bet, UUID> {
    List<Bet> findByUserIdOrderByCreatedAtDesc(UUID userId);
}