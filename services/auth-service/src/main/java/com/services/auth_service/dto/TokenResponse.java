package com.services.auth_service.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        Long expiresIn
) {}