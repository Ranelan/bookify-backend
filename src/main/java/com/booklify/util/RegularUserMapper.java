package com.booklify.util;
import com.booklify.domain.RegularUser;
import com.booklify.dto.RegularUserDto;

public class RegularUserMapper {

    public static RegularUserDto toDto(RegularUser user) {
        RegularUserDto dto = new RegularUserDto();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setBio(user.getBio());
        dto.setSellerRating(user.getSellerRating());
        dto.setDateJoined(user.getDateJoined());
        dto.setLastLogin(user.getLastLogin());
        // Don't set password in DTO for security reasons
        return dto;
    }

    public static RegularUser toEntity(RegularUserDto dto) {
        return new RegularUser.RegularUserBuilder()
                .setId(dto.getId())
                .setFullName(dto.getFullName())
                .setEmail(dto.getEmail())
                .setBio(dto.getBio())
                .setSellerRating(dto.getSellerRating())
                .setPassword(dto.getPassword())  // Add this line
                .setDateJoined(dto.getDateJoined())
                .setLastLogin(dto.getLastLogin())
                .build();
    }
}
