package com.booklify.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @OneToOne
    @JsonIgnore
    private RegularUser regularUser;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CartItems> cartItems =  new ArrayList<>();

    public Cart() {
    }

    public Cart(Long cartId, RegularUser regularUser, List<CartItems> cartItems) {
        this.cartId = cartId;
        this.regularUser = regularUser;
        this.cartItems = cartItems;
    }

    public Cart(CartBuilder cartBuilder) {
        this.cartId = cartBuilder.cartId;
        this.regularUser = cartBuilder.regularUser;
        this.cartItems = cartBuilder.cartItems;
    }

    public Long getCartId() {
        return cartId;
    }

    public RegularUser getRegularUser() {
        return regularUser;
    }

    public List<CartItems> getCartItems() {
        return cartItems;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cartId=" + cartId +
                '}';
    }

    public void setRegularUser(RegularUser user) {
        this.regularUser = user;
    }

    public static class CartBuilder {
        private Long cartId;
        private RegularUser regularUser;
        private List<CartItems> cartItems = new ArrayList<>();

        public CartBuilder setCartId(Long cartId) {
            this.cartId = cartId;
            return this;
        }

        public CartBuilder setRegularUser(RegularUser regularUser) {
            this.regularUser = regularUser;
            return this;
        }

        public CartBuilder setCartItems(List<CartItems> cartItems) {
            this.cartItems = cartItems;
            return this;
        }

        public CartBuilder copy(Cart cart) {
            this.cartId = cart.cartId;
            this.regularUser = cart.regularUser;
            this.cartItems = cart.cartItems;
            return this;
        }

        public Cart build() {
            return new Cart(this);
        }
    }
}
