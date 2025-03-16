package com.mowbie.mowbie_backend.model;

import lombok.Getter;

@Getter
public class User {
    private Long userId;
    private String username;
    private String email;
    private String phoneNumber;
    private String avatarPath;
    private String password;
    private Boolean isActive;
    private String userRole;

    public User(Long userId, String username, String email, String phoneNumber, String avatarPath, String password, Boolean isActive, String userRole) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.avatarPath = avatarPath;
        this.password = password;
        this.isActive = isActive;
        this.userRole = userRole;
    }
}

