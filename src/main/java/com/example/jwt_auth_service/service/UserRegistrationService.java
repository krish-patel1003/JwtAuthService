package com.example.jwt_auth_service.service;

import com.example.jwt_auth_service.entity.User;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.jwt_auth_service.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(User user) {
        if(userRepository.existsByUsername(user.getUsername()) ||
        userRepository.existsByEmail(user.getEmail())) {

            throw new ValidationException("Username or Email already Exists");
        }

        User newuser = new User();
        newuser.setUsername(user.getUsername());
        newuser.setEmail(user.getEmail());
        newuser.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(newuser);
    }
}
