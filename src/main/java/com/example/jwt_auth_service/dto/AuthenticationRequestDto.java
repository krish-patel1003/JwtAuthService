package com.example.jwt_auth_service.dto;

public record AuthenticationRequestDto(
        String username,
        String password
) {
}
