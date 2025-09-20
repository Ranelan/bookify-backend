package com.booklify.service.impl;

import com.booklify.domain.Cart;
import com.booklify.domain.RegularUser;
import com.booklify.repository.CartRepository;
import com.booklify.repository.RegularUserRepository;
import com.booklify.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService implements ICartService {

    @Autowired
    private CartRepository cartRepository;


    @Autowired
    private RegularUserRepository regularUserRepository;

    public Cart save(Cart cart) {
        if (cart.getRegularUser() != null) {
            if (cart.getRegularUser().getId() != null) {
                RegularUser user = regularUserRepository.findById(cart.getRegularUser().getId())
                        .orElseThrow(() -> new RuntimeException("User not found"));
                cart.setRegularUser(user);
            } else {
                throw new IllegalArgumentException("RegularUser must already exist and have a valid id before attaching to Cart.");
            }
        }
        return cartRepository.save(cart);
    }

    @Override
    public Cart findById(Long aLong) {
        return cartRepository.findById(aLong).orElse(null);
    }

    @Override
    public Cart findByRegularUserId(Long id) {
        return cartRepository.findByRegularUserId(id).orElse(null);
    }

    @Override
    public Cart updateCartItemQuantity(Long cartId, Long bookId, int quantity) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart != null) {
            cart.getCartItems().removeIf(item -> item.getBook().getBookID().equals(bookId) && quantity == 0);
            cart.getCartItems().stream()
                    .filter(item -> item.getBook().getBookID().equals(bookId))
                    .findFirst()
                    .ifPresent(item -> item.setQuantity(quantity));
            return cartRepository.save(cart);
        }
        return null;
    }

    @Override
    public Cart update(Cart entity) {
        // Ensure all cart items reference the parent cart
        if (entity.getCartItems() != null) {
            entity.getCartItems().forEach(item -> item.setCart(entity));
        }
        return cartRepository.save(entity);
    }

    @Override
    public boolean deleteById(Long aLong) {
        return cartRepository.findById(aLong).map(cart -> {
            cartRepository.deleteById(aLong);
            return true;
        }).orElse(false);
    }
}
