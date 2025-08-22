package com.booklify.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @Column(nullable = false)
    protected String fullName;
    @Column(nullable = false, unique = true)
    protected String email;
    @Column(nullable = false)
    protected String password;

    @CreationTimestamp // Automatically sets the date when the user is created
    @Column(name = "date_joined", nullable = false, updatable = false)
    protected LocalDateTime dateJoined;

    public User() {
        // Default constructor
    }

    public User(Long id, String fullName, String email, String password, LocalDateTime dateJoined) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.dateJoined = dateJoined;
    }

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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", dateJoined=" + dateJoined +
                '}';
    }

}
