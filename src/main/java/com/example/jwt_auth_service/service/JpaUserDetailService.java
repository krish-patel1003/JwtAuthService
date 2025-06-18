package com.example.jwt_auth_service.service;

import com.example.jwt_auth_service.exception.EmailVerificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.jwt_auth_service.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class JpaUserDetailService implements UserDetailsService {

    @Value("${email-verification.required}")
    private boolean emailVerificationRequired;

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {


        return userRepository.findByUsername(username).map(user -> {
            if (emailVerificationRequired && !user.isEmailVerified()) {
                throw new EmailVerificationException(
                        "Your email is not verified");
            }
            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .build();
        }).orElseThrow(() -> new UsernameNotFoundException(
                "User with username [%s] not found".formatted(username)));
    }
}
