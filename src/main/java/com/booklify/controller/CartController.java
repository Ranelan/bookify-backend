package com.booklify.controller;

import com.booklify.domain.Cart;
import com.booklify.service.impl.CartService;
import com.booklify.dto.CartCreateDto;
import com.booklify.domain.RegularUser;
import com.booklify.repository.RegularUserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private RegularUserRepository regularUserRepository;

    @PostMapping("/create")
    public ResponseEntity<String> createCart(@Valid @RequestBody CartCreateDto cartCreateDto) {
        RegularUser user = regularUserRepository.findById(cartCreateDto.getRegularUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = new Cart();
        cart.setRegularUser(user);
        Cart savedCart = cartService.save(cart);
        return ResponseEntity.ok("Cart created with ID: " + savedCart.getCartId());
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Cart> getCartById(@PathVariable Long id) {
        Cart cart = cartService.findById(id);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/getByUserId/{userId}")
    public ResponseEntity<Cart> getCartByUserId(@PathVariable Long userId) {
        Cart cart = cartService.findByRegularUserId(userId);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateCart(@RequestBody Cart cart) {
        Cart updatedCart = cartService.update(cart);
        return ResponseEntity.ok("Cart updated with ID: " + updatedCart.getCartId());
    }

    @PutMapping("/updateCartItemsQuantity/{cartId}/{bookId}/{quantity}")
    public ResponseEntity<String> updateCartItemsQuantity(
            @PathVariable Long cartId,
            @PathVariable Long bookId,
            @PathVariable int quantity) {
        Cart updatedCart = cartService.updateCartItemQuantity(cartId, bookId, quantity);
        if (updatedCart == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Cart items quantity updated for Cart ID: " + updatedCart.getCartId());
    }

    @DeleteMapping("/clear/{cartId}")
    public ResponseEntity<String> clearCart(@PathVariable Long cartId) {
        cartService.deleteById(cartId);
        return ResponseEntity.ok("Cart cleared with ID: " + cartId);
    }

}
