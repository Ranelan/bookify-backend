package com.booklify.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CartDto {
    private Long cartId;

    @Valid
    @NotNull(message = "RegularUser is required")
    private RegularUserDto regularUser;

    // Optionally include cartItems if needed for creation
    // private List<CartItemsDto> cartItems;

    public CartDto() {}

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public RegularUserDto getRegularUser() {
        return regularUser;
    }

    public void setRegularUser(RegularUserDto regularUser) {
        this.regularUser = regularUser;
    }

    // Add getters/setters for cartItems if needed
}

