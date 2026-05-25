package com.services.auth_service.service;

import com.services.auth_service.dto.LoginRequest;
import com.services.auth_service.dto.TokenResponse;
import com.services.auth_service.model.RefreshToken;
import com.services.auth_service.model.User;
import com.services.auth_service.repository.RefreshTokenRepository;
import com.services.auth_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() ->
                        new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        String accessToken = jwtService.generateToken(user);

        String refreshToken = UUID.randomUUID().toString();
        refreshRepo.save(RefreshToken.builder()
                .userId(user.getId())
                .token(refreshToken)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .revoked(false)
                .build());

        return new TokenResponse(accessToken, refreshToken, 900L);
    }
}

