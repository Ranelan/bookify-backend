package com.booklify.factory;
import com.booklify.domain.Book;
import com.booklify.domain.RegularUser;
import com.booklify.domain.enums.BookCondition;
import com.booklify.util.Helper;

import java.time.LocalDateTime;

public class BookFactory {

    public static Book createBook(String isbn, String title, String author, String publisher,
                                  BookCondition condition, Double price, String description,
                                  byte[] image, RegularUser user) {
        // Validations
        if (!Helper.isValidISBN(isbn)) {
            throw new IllegalArgumentException("Invalid ISBN.");
        }

        if (Helper.isNullOrEmpty(title)) {
            throw new IllegalArgumentException("Title cannot be empty.");
        }

        if (condition == null) {
            throw new IllegalArgumentException("Condition must not be null.");
        }

        if (price == null || price < 0) {
            throw new IllegalArgumentException("Price must be a positive number.");
        }

//        if (seller == null) {
//            throw new IllegalArgumentException("Seller must not be null.");
//        }
        final long MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB
        if (image == null || image.length == 0) {
            throw new IllegalArgumentException("Image must not be empty.");
        }
        if (image.length > MAX_IMAGE_SIZE) {
            throw new IllegalArgumentException("Image size exceeds maximum limit of 5MB");
        }

        if (user == null) {
            throw new IllegalArgumentException("Seller must not be null.");
        }


        return new Book.Builder()
                .setIsbn(isbn)
                .setTitle(title)
                .setAuthor(author)
                .setPublisher(publisher)
                .setCondition(condition)
                .setPrice(price)
                .setDescription(description)
                .setUploadedDate(LocalDateTime.now())
                .setUser(user)
                .setImage(image)
                .build();
    }
}

