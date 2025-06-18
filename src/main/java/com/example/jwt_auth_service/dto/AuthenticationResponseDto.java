package com.example.jwt_auth_service.dto;

import java.util.UUID;

public record AuthenticationResponseDto(
        String accessToken,
        UUID refreshToken
){
}
