package com.example.jwt_auth_service.service;

import com.example.jwt_auth_service.config.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;
    private final JwtEncoder jwtEncoder;

    public String generateToken(final String username) {
        final var issuedAt = Instant.now();

        final var claimSet = JwtClaimsSet.builder()
                .subject(username)
                .issuer(jwtProperties.getIssuer())
                .issuedAt(issuedAt)
                .expiresAt(issuedAt.plus(jwtProperties.getTtl()))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claimSet)).getTokenValue();
    }
}
