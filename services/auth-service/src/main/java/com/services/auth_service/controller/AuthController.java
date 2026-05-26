package com.services.auth_service.controller;

import com.services.auth_service.dto.LoginRequest;
import com.services.auth_service.dto.RefreshRequest;
import com.services.auth_service.dto.TokenResponse;
import com.services.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(
            @Valid @RequestBody RefreshRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> login(
            @Valid @RequestBody RefreshRequest request) {
        authService.logOut(request);
        return ResponseEntity.ok().body("OK");
    }
}
