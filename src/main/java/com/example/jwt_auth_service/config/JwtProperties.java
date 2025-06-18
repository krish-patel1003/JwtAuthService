package com.example.jwt_auth_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;

@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {
    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;
    private String issuer;
    private Duration ttl;
}
