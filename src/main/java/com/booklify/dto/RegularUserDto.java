package com.booklify.dto;

import java.time.LocalDateTime;

public class RegularUserDto {
    private Long id;
    private String fullName;
    private String email;
    private String password;  // Added this field
    private LocalDateTime dateJoined;
    private double sellerRating = 0.0;
    private String bio;
    private LocalDateTime lastLogin;

    public RegularUserDto() {
    }

    public RegularUserDto(Long id, String fullName, String email, LocalDateTime dateJoined, double sellerRating, String bio, LocalDateTime lastLogin) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.dateJoined = dateJoined;
        this.sellerRating = sellerRating;
        this.bio = bio;
        this.lastLogin = lastLogin;
    }

    // Add password getter and setter
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Rest of the existing getters and setters remain the same
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

    public LocalDateTime getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(LocalDateTime dateJoined) {
        this.dateJoined = dateJoined;
    }

    public double getSellerRating() {
        return sellerRating;
    }

    public void setSellerRating(double sellerRating) {
        this.sellerRating = sellerRating;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
}
