package com.example.jwt_auth_service.controller;

import com.example.jwt_auth_service.dto.RegistrationRequestDto;
import com.example.jwt_auth_service.dto.RegistrationResponseDto;
import com.example.jwt_auth_service.service.EmailVerificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.example.jwt_auth_service.mapper.UserRegistrationMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.jwt_auth_service.service.UserRegistrationService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RegistrationController {

    @Value("${email-verification.required}")
    private boolean emailVerificationRequired;

    private final EmailVerificationService emailVerificationService;

    private final UserRegistrationService userRegistrationService;

    private final UserRegistrationMapper userRegistrationMapper;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponseDto> registerUser(
            @Valid @RequestBody final RegistrationRequestDto registrationDto) {

        final var registerUser = userRegistrationService.registerUser(
                userRegistrationMapper.toEntity(registrationDto));

        if(emailVerificationRequired) {
            emailVerificationService.sendVerificationToken(registerUser.getId(), registerUser.getEmail());
        }

        return ResponseEntity.ok(
                userRegistrationMapper.toRegistrationResponseDto(registerUser, emailVerificationRequired));
    }

}
