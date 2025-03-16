package com.mowbie.mowbie_backend.dto;

import lombok.Getter;

@Getter
public class UserDTO {
    private Long userId;
    private String username;
    private String email;
    private String phoneNumber;
    private String avatarPath;
    private Boolean isActive;
    private String userRole;

    public UserDTO(Long userId, String username, String email, String phoneNumber, String avatarPath, Boolean isActive, String userRole) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.avatarPath = avatarPath;
        this.isActive = isActive;
        this.userRole = userRole;
    }
}
