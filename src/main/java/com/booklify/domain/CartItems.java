package com.booklify.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class CartItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    @ManyToOne
    @JsonBackReference
    private Cart cart;

    @ManyToOne
    private Book book;
    private int quantity;


    public CartItems() {
    }

    public CartItems(Long cartItemId, Cart cart, Book book, int quantity) {
        this.cartItemId = cartItemId;
        this.cart = cart;
        this.book = book;
        this.quantity = quantity;
    }

    public CartItems(CartItemsBuilder cartItemsBuilder) {
        this.cartItemId = cartItemsBuilder.cartItemId;
        this.cart = cartItemsBuilder.cart;
        this.book = cartItemsBuilder.book;
        this.quantity = cartItemsBuilder.quantity;
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public Book getBook() {
        return book;
    }

    public Cart getCart() {
        return cart;
    }

    @Override
    public String toString() {
        return "CartItems{" +
                "cartItemId=" + cartItemId +
                ", book=" + (book != null ? book.getBookID() : null) +
                ", quantity=" + quantity +
                '}';
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCart(Cart entity) {
        this.cart = entity;
    }

    public static class CartItemsBuilder {
        private Long cartItemId;
        private Cart cart;
        private Book book;
        private int quantity;


        public CartItemsBuilder setCart(Cart cart) {
            this.cart = cart;
            return this;
        }

        public CartItemsBuilder setBook(Book book) {
            this.book = book;
            return this;
        }

        public CartItemsBuilder setQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public CartItemsBuilder copy(CartItems cartItems) {
            this.cartItemId = cartItems.cartItemId;
            this.cart = cartItems.cart;
            this.book = cartItems.book;
            this.quantity = cartItems.quantity;
            return this;
        }

        public CartItems build() {
            return new CartItems(this);
        }
    }
}
