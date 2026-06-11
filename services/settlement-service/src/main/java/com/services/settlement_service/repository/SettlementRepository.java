package com.services.settlement_service.repository;

import com.services.settlement_service.model.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SettlementRepository extends JpaRepository<Settlement, UUID> {
    boolean existsByBetId(UUID betId);
}
