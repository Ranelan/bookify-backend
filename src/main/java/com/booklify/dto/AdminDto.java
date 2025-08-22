package com.booklify.dto;



import com.booklify.domain.Admin;
import com.booklify.domain.enums.Permissions;

import java.time.LocalDateTime;
import java.util.List;

public class AdminDto {

    private Long id;
    private String fullName;
    private String email;
    private String password = null;
    private LocalDateTime dateJoined;
    private LocalDateTime lastLogin;
    private List<Permissions> permissions;

    // Constructors
    public AdminDto() {
    }

    public AdminDto(Long id, String fullName, String email, String password,
                    LocalDateTime dateJoined, LocalDateTime lastLogin, List<Permissions> permissions) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.dateJoined = dateJoined;
        this.lastLogin = lastLogin;
        this.permissions = permissions;
    }

    // Static mapping from entity to DTO
    public static AdminDto fromEntity(Admin admin) {
        return new AdminDto(
                admin.getId(),
                admin.getFullName(),
                admin.getEmail(),
                null, // hide password
                admin.getDateJoined(),
                admin.getLastLogin(),
                admin.getPermissions()
        );
    }

    // Static mapping from DTO to entity
    public static Admin toEntity(AdminDto dto) {
        return new Admin.AdminBuilder()
                .setId(dto.getId())
                .setFullName(dto.getFullName())
                .setEmail(dto.getEmail())
                .setPassword(dto.getPassword())
                .setDateJoined(dto.getDateJoined())
                .setLastLogin(dto.getLastLogin())
                .setPermissions(dto.getPermissions())
                .build();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(LocalDateTime dateJoined) {
        this.dateJoined = dateJoined;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public List<Permissions> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permissions> permissions) {
        this.permissions = permissions;
    }
}

