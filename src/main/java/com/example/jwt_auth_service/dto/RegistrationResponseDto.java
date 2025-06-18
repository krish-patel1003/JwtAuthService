package com.example.jwt_auth_service.dto;


public record RegistrationResponseDto(
        String username,
        String email,
        boolean emailVerificationRequired
) {
}
