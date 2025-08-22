package com.booklify.factory;

import com.booklify.domain.Admin;

import com.booklify.domain.enums.Permissions;
import com.booklify.util.Helper;

import java.time.LocalDateTime;
import java.util.List;

public class AdminFactory {

    public static Admin createAdmin(String fullName, String email, String password, LocalDateTime dateJoined
            , LocalDateTime lastLogin, List<Permissions> permissions){

        if (Helper.isNullOrEmpty(fullName) ||
                !Helper.isValidEmail(email) ||
                !Helper.isValidPassword(password) ||
                !Helper.isValidDateTime(dateJoined) ||
                !Helper.isValidDateTime(lastLogin) ||
                permissions == null || permissions.isEmpty())
            return null;

        {
            return new Admin.AdminBuilder()
                    .setFullName(fullName)
                    .setEmail(email)
                    .setPassword(password)
                    .setDateJoined(dateJoined)
                    .setLastLogin(lastLogin)
                    .setPermissions(permissions)
                    .build();
        }
    }
}
