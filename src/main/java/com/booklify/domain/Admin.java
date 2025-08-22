package com.booklify.domain;

import com.booklify.domain.enums.Permissions;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@DiscriminatorValue("Admin")
public class Admin extends User {

    @Column(nullable = true)
    private LocalDateTime lastLogin;

    @ElementCollection(targetClass = Permissions.class)
    @Enumerated(EnumType.STRING)
    private List<Permissions> permissions; //List of permissions granted to the admin


    public Admin() {
        super();
    }

    public Admin(Long id, String fullName, String email, String password, LocalDateTime dateJoined
            , LocalDateTime lastLogin, List<Permissions> permissions)
    {
        super(id, fullName, email, password, dateJoined);
        this.lastLogin = lastLogin;
        this.permissions = permissions;
    }

    public Admin(AdminBuilder adminBuilder) {

        this.id = adminBuilder.id;
        this.fullName = adminBuilder.fullName;
        this.email = adminBuilder.email;
        this.password = adminBuilder.password;
        this.dateJoined = adminBuilder.dateJoined;
        this.lastLogin = adminBuilder.lastLogin;
        this.permissions = adminBuilder.permissions;

    }


    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public List<Permissions> getPermissions() {
        return permissions;
    }

    public static class AdminBuilder {
        private Long id;
        private String fullName;
        private String email;
        private String password;
        private LocalDateTime dateJoined;
        private LocalDateTime lastLogin;
        private List<Permissions> permissions;

        public AdminBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public AdminBuilder setFullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public AdminBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public AdminBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public AdminBuilder setDateJoined(LocalDateTime dateJoined) {
            this.dateJoined = dateJoined;
            return this;
        }

        public AdminBuilder setLastLogin(LocalDateTime lastLogin) {
            this.lastLogin = lastLogin;
            return this;
        }

        public AdminBuilder setPermissions(List<Permissions> permissions) {
            this.permissions = permissions;
            return this;
        }

        // Copy constructor to copy properties from an existing Admin object
        public AdminBuilder copy (Admin admin) {
            this.id = admin.id;
            this.fullName = admin.fullName;
            this.email = admin.email;
            this.password = admin.password;
            this.dateJoined = admin.dateJoined;
            this.lastLogin = admin.lastLogin;
            this.permissions = admin.permissions;
            return this;
        }

        public Admin build() {
            if(this.lastLogin == null){
                this.lastLogin = LocalDateTime.now();
            }
            return new Admin(this);
        }
    }
}
