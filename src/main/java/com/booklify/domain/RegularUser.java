package com.booklify.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("RegularUser")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RegularUser extends User{

    @Column(nullable = true)
    private Double sellerRating = 0.0;

    @Lob
    @Column(nullable = true)
    private String bio;

    @Column(nullable = true)
    private LocalDateTime lastLogin;

    public RegularUser() {
        super();
    }

    public RegularUser(Long id, String fullName, String email, String password, LocalDateTime dateJoined,
                       Double sellerRating, String bio, LocalDateTime lastLogin) {
        super(id, fullName, email, password, dateJoined);
        this.sellerRating = sellerRating;
        this.bio = bio;
        this.lastLogin = lastLogin;
    }

    public RegularUser(RegularUserBuilder regularUserBuilder) {
        this.id = regularUserBuilder.id;
        this.fullName = regularUserBuilder.fullName;
        this.email = regularUserBuilder.email;
        this.password = regularUserBuilder.password;
        this.dateJoined = regularUserBuilder.dateJoined;
        this.sellerRating = regularUserBuilder.sellerRating;
        this.bio = regularUserBuilder.bio;
        this.lastLogin = regularUserBuilder.lastLogin;
    }

    public double getSellerRating() {
        return sellerRating;
    }

    public String getBio() {
        return bio;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setSellerRating(Double sellerRating) {
        this.sellerRating = sellerRating;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public String toString() {
        return "RegularUser{" +
                ", id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", dateJoined=" + dateJoined +
                "sellerRating=" + sellerRating +
                ", bio='" + bio + '\'' +
                ", lastLogin=" + lastLogin +
                '}';
    }

    public static class RegularUserBuilder {
        private Long id;
        private String fullName;
        private String email;
        private String password;
        private LocalDateTime dateJoined;
        private Double sellerRating = 0.0;
        private String bio;
        private LocalDateTime lastLogin;

        public RegularUserBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public RegularUserBuilder setFullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public RegularUserBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public RegularUserBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public RegularUserBuilder setDateJoined(LocalDateTime dateJoined) {
            this.dateJoined = dateJoined;
            return this;
        }

        public RegularUserBuilder setSellerRating(Double sellerRating) {
            this.sellerRating = sellerRating;
            return this;
        }

        public RegularUserBuilder setBio(String bio) {
            this.bio = bio;
            return this;
        }

        public RegularUserBuilder setLastLogin(LocalDateTime lastLogin) {
            this.lastLogin = lastLogin;
            return this;
        }

        public RegularUserBuilder copy(RegularUser regularUser) {
            this.id = regularUser.id;
            this.fullName = regularUser.fullName;
            this.email = regularUser.email;
            this.password = regularUser.password;
            this.dateJoined = regularUser.dateJoined;
            this.sellerRating = regularUser.sellerRating;
            this.bio = regularUser.bio;
            this.lastLogin = regularUser.lastLogin;
            return this;
        }



        public RegularUser build() {
            return new RegularUser(this);
        }
    }
}
