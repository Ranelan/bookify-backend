package com.booklify.factory;

import com.booklify.domain.RegularUser;
import com.booklify.util.Helper;

import java.time.LocalDateTime;

public class RegularUserFactory {

    public static RegularUser createRegularUser(String fullName, String email, String password, LocalDateTime dateJoined,
                                                Double sellerRating, String bio, LocalDateTime lastLogin){

        if (Helper.isNullOrEmpty(fullName)||
                !Helper.isValidEmail(email)||
                !Helper.isValidPassword(password)||
                !Helper.isValidDateTime(dateJoined)||
                !Helper.isValidSellerRating(sellerRating)||
                Helper.isNullOrEmpty(bio)||
                !Helper.isValidDateTime(lastLogin)) {

            return null;
        }

        // Accept password as already encoded (do not encode here)
        return new RegularUser.RegularUserBuilder()
                .setFullName(fullName)
                .setEmail(email)
                .setPassword(password)
                .setDateJoined(dateJoined)
                .setSellerRating(sellerRating)
                .setBio(bio)
                .setLastLogin(lastLogin)
                .build();

    }
}





