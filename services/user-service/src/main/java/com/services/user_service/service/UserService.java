package com.services.user_service.service;

import com.services.user_service.service.JwtService;
import com.services.user_service.dto.RegisterRequest;
import com.services.user_service.dto.UserResponse;
import com.services.user_service.exception.UserAlreadyExistsException;
import com.services.user_service.model.User;
import com.services.user_service.model.Wallet;
import com.services.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("Email already in use: " + request.email());
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new UserAlreadyExistsException("Username already taken: " + request.username());
        }

        User user = User.builder()
                .email(request.email())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(User.Role.USER)
                .build();

        Wallet wallet = Wallet.builder()
                .user(user)
                .balance(BigDecimal.ZERO)
                .currency("USD")
                .build();

        user.setWallet(wallet);
        User saved = userRepository.save(user);
        userRepository.flush();
        User refreshed = userRepository.findById(saved.getId()).orElseThrow();
        String token = jwtService.generateToken(saved);

        return toResponse(refreshed, token);
    }

    public UserResponse toResponse(User user, String token) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getRole().name(),
                user.getWallet() != null ? user.getWallet().getBalance() : BigDecimal.ZERO,
                user.getCreatedAt(),
                token
        );
    }
}