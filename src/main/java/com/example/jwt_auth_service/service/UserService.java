package com.example.jwt_auth_service.service;

import com.example.jwt_auth_service.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.jwt_auth_service.repository.UserRepository;
import static org.springframework.http.HttpStatus.GONE;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserByUsername(final String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(GONE,
                        "The user account has been deleted or inactive"));
    }
}
