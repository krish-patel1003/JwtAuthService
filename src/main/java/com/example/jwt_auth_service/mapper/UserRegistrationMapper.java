package com.example.jwt_auth_service.mapper;

import com.example.jwt_auth_service.dto.RegistrationRequestDto;
import com.example.jwt_auth_service.dto.RegistrationResponseDto;
import com.example.jwt_auth_service.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationMapper {

    public User toEntity(RegistrationRequestDto registrationRequestDto) {
        final var user = new User();

        user.setEmail(registrationRequestDto.email());
        user.setUsername(registrationRequestDto.username());
        user.setPassword(registrationRequestDto.password());

        return user;
    }

    public RegistrationResponseDto toRegistrationResponseDto(
            final User user, final boolean emailVerificationRequired) {
        return new RegistrationResponseDto(
                user.getUsername(),
                user.getEmail(),
                emailVerificationRequired
        );
    }
}
