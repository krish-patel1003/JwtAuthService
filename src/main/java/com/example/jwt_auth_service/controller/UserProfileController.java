package com.example.jwt_auth_service.controller;

import com.example.jwt_auth_service.dto.UserProfileDto;
import lombok.RequiredArgsConstructor;
import com.example.jwt_auth_service.mapper.UserMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.jwt_auth_service.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserProfileController {

    private final UserService userService;

    private final UserMapper userMapper;

    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getUserProfile(final Authentication authentication) {

        final var user = userService.getUserByUsername(authentication.getName());

        return ResponseEntity.ok(userMapper.toUserProfileDto(user));
    }
}
