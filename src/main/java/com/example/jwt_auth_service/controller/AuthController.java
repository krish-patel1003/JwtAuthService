package com.example.jwt_auth_service.controller;

import com.example.jwt_auth_service.dto.AuthenticationRequestDto;
import com.example.jwt_auth_service.dto.AuthenticationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.jwt_auth_service.service.AuthenticationService;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> authenticate(
            @RequestBody final AuthenticationRequestDto request
            ) {

        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponseDto> refreshToken(
            @RequestParam UUID refreshToken) {
        
        AuthenticationResponseDto response = authenticationService.refreshToken(refreshToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> revokeToken(@RequestParam UUID refreshToken) {
        authenticationService.revokeRefreshToken(refreshToken);
        return ResponseEntity.noContent().build();
    }

}
