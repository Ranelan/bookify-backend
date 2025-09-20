package com.booklify.dto;

import jakarta.validation.constraints.NotNull;

public class CartCreateDto {
    @NotNull(message = "User ID is required")
    private Long regularUserId;

    public Long getRegularUserId() {
        return regularUserId;
    }

    public void setRegularUserId(Long regularUserId) {
        this.regularUserId = regularUserId;
    }
}

