package com.example.jwt_auth_service.controller;

import com.example.jwt_auth_service.dto.UserProfileDto;
import com.example.jwt_auth_service.mapper.UserMapper;
import com.example.jwt_auth_service.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth/email")
@RequiredArgsConstructor
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    private final UserMapper userMapper;

    @PostMapping("/resend-verification")
    public ResponseEntity<Void> resendVerificationLink(@RequestParam String email){
        emailVerificationService.resendVerificationToken(email);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/verify")
    public ResponseEntity<UserProfileDto> verifyEmail(@RequestParam("uid") String encryptedId,
                                                      @RequestParam("t") String token) {
        final var verifiedUser = emailVerificationService.verifyEmail(UUID.fromString(encryptedId), token);

        return ResponseEntity.ok(userMapper.toUserProfileDto(verifiedUser));
    }
}
