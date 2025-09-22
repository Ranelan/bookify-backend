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
    public Cart getCartByUserId(Long regularUserId) {
        return cartRepository.findByRegularUser_Id(regularUserId).orElse(null);
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
    public Cart update(Cart cartFromClient) {
        // 1. READ: Fetch the existing cart from the database using the ID from the client object.
        // This gives us the real, managed entity with all its associations intact.
        Cart existingCart = cartRepository.findById(cartFromClient.getCartId())
                .orElseThrow(() -> new RuntimeException("Cart not found with id: " + cartFromClient.getCartId()));

        // The 'existingCart' object now correctly has the RegularUser attached to it.

        // 2. MODIFY: Clear the old items and add the new/updated items from the client.
        // This is a safe way to synchronize the collection.
        existingCart.getCartItems().clear();
        if (cartFromClient.getCartItems() != null) {
            cartFromClient.getCartItems().forEach(item -> {
                item.setCart(existingCart); // IMPORTANT: Ensure bidirectional link is correct
                existingCart.getCartItems().add(item);
            });
        }

        // 3. WRITE: Save the modified 'existingCart'.
        // JPA will now correctly persist the changes to the cartItems while preserving the
        // all-important RegularUser association.
        return cartRepository.save(existingCart);
    }

    @Override
    public boolean deleteById(Long aLong) {
        return cartRepository.findById(aLong).map(cart -> {
            cartRepository.deleteById(aLong);
            return true;
        }).orElse(false);
    }
}
