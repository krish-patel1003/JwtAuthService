package com.example.jwt_auth_service.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.example.jwt_auth_service.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
@RequiredArgsConstructor
public class JwtConfig {

    private final JwtProperties jwtProperties;

    @Bean
    public JwtEncoder jwtEncoder() {
        var jwk = new RSAKey.Builder(jwtProperties.getPublicKey())
                .privateKey(jwtProperties.getPrivateKey())
                .build();

        return new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(jwk)));
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(jwtProperties.getPublicKey()).build();
    }
}
