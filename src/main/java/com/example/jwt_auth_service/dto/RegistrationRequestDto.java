package com.example.jwt_auth_service.dto;

public record RegistrationRequestDto(
        String username,
        String email,
        String password
) {
}
