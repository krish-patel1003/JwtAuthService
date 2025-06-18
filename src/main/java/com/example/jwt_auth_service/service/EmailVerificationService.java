package com.example.jwt_auth_service.service;

import com.example.jwt_auth_service.entity.User;
import com.example.jwt_auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.GONE;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailVerificationService {

    @Value("${email-verification.base-url}")
    private String baseUrl;

    private final OtpService otpService;

    private final UserRepository userRepository;

    private final JavaMailSender javaMailSender;

    @Async
    public void sendVerificationToken(UUID userId, String email) {
        final var token = otpService.generateAndStoreOtp(userId);

        final var emailVerificationUrl = baseUrl.formatted(userId, token);

        final var emailText = """
                Click the link to verify your email: """ + emailVerificationUrl;

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Email Verification");
        message.setFrom("System");
        message.setText(emailText);

        javaMailSender.send(message);
    }

    @Transactional
    public User verifyEmail(UUID userId, String token) {
        if(!otpService.isOtpValid(userId, token)) {
            throw new ResponseStatusException(BAD_REQUEST,
                    "Token is invalid or expired");
        }

        otpService.deleteOtp(userId);

        final var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(GONE,
                        "User account has been deleted or Deactivated"));

        if(user.isEmailVerified()) {
            throw new ResponseStatusException(BAD_REQUEST,
                    "Email already verified");
        }

        user.setEmailVerified(true);

        return user;
    }

    public void resendVerificationToken(String email) {
        userRepository.findByEmail(email).filter(user -> !user.isEmailVerified())
                .ifPresentOrElse(user -> sendVerificationToken(user.getId(), user.getEmail()),
                        () -> log.warn("Attempt to resend verification token for non existing or already validated email: [{}]", email));

    }

}
