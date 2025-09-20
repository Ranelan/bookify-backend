package com.booklify.service;

import com.booklify.domain.Cart;

public interface ICartService extends IService <Cart, Long> {

    Cart findByRegularUserId(Long id);
    Cart updateCartItemQuantity(Long cartId, Long bookId, int quantity);
}
