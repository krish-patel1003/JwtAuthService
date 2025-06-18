package com.example.jwt_auth_service.service;

import com.example.jwt_auth_service.dto.AuthenticationRequestDto;
import com.example.jwt_auth_service.dto.AuthenticationResponseDto;
import com.example.jwt_auth_service.entity.RefreshToken;
import com.example.jwt_auth_service.entity.User;
import com.example.jwt_auth_service.repository.RefreshTokenRepository;
import com.example.jwt_auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Value("${jwt.refresh-token-ttl}")
    private Duration refreshTokenTtl;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    public AuthenticationResponseDto authenticate(
            final AuthenticationRequestDto request
            ) {

        // Authenticate User
        final var authToken = UsernamePasswordAuthenticationToken.unauthenticated(
                request.username(), request.password());

        final var authentication = authenticationManager.authenticate(authToken);

        // Generate Jwt access token
        final var accessToken = jwtService.generateToken(request.username());

        // Fetch user and create refresh token
        final var user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User with username[%s] not found".formatted(request.username())));


        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(Instant.now().plus(refreshTokenTtl));
        refreshTokenRepository.save(refreshToken);

        return new AuthenticationResponseDto(accessToken, refreshToken.getId());
    }

    public AuthenticationResponseDto refreshToken(UUID refreshtoken) {
        final var refreshToken = refreshTokenRepository
                .findByIdAndExpiresAtAfter(refreshtoken, Instant.now())
                .orElseThrow(() -> new BadCredentialsException("Invalid or Expired refresh Token")
                );

        final var newAccessToken = jwtService.generateToken(refreshToken.getUser().getUsername());

        return new AuthenticationResponseDto(newAccessToken, refreshtoken);
    }

    public void revokeRefreshToken(UUID refreshToken) {
        refreshTokenRepository.deleteById(refreshToken);
    }
}
