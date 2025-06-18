package com.example.jwt_auth_service.dto;

public record UserProfileDto(
        String email, String username, boolean emailVerified
) {
}
