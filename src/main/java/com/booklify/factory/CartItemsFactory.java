package com.booklify.factory;

import com.booklify.domain.Book;
import com.booklify.domain.Cart;
import com.booklify.domain.CartItems;

public class CartItemsFactory {

    public static CartItems createCartItems( Cart cart, Book book, int quantity){
        if (cart == null ||
                book == null ||
                quantity <= 0
        )
            return null;
    return new CartItems.CartItemsBuilder()
            .setCart(cart)
            .setBook(book)
            .setQuantity(quantity)
            .build();
    }
}
