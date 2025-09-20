package com.booklify.factory;

import com.booklify.domain.Cart;
import com.booklify.domain.CartItems;
import com.booklify.domain.RegularUser;

import java.util.List;

public class CartFactory {

    public static Cart createCart(RegularUser regularUser, List<CartItems> cartItem){
        if (regularUser == null || cartItem == null) {
            return null;
        }

        return new Cart.CartBuilder()
                .setRegularUser(regularUser)
                .setCartItems(cartItem)
                .build();
    }
}
