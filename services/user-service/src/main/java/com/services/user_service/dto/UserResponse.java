package com.services.user_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        String username,
        String role,
        BigDecimal walletBalance,
        LocalDateTime createdAt,
        String accessToken
) {}