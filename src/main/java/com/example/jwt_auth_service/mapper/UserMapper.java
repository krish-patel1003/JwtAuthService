package com.example.jwt_auth_service.mapper;

import com.example.jwt_auth_service.dto.UserProfileDto;
import com.example.jwt_auth_service.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserProfileDto toUserProfileDto(final User user) {
        return new UserProfileDto(user.getEmail(), user.getUsername(), user.isEmailVerified());
    }
}
