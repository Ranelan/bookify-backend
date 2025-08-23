// Thaakirah Watson, 230037550
package com.booklify.factory;

import com.booklify.domain.Book;
import com.booklify.domain.RegularUser;
import com.booklify.domain.Review;
import com.booklify.domain.User;

import java.time.LocalDate;

public class ReviewFactory {

    private static ReviewFactory reviewFactory;

    private ReviewFactory() {}

    public static ReviewFactory getInstance() {
        if (reviewFactory == null) {
            reviewFactory = new ReviewFactory();
        }
        return reviewFactory;
    }

    public static Review createReview(int rating, String comment, LocalDate date, RegularUser user, Book book) {
        return new Review.Builder()
                .setReviewRating(rating)
                .setReviewComment(comment)
                .setReviewDate(date)
                .setUser(user)
                .setBook(book)
                .build();
    }
    public static Review createReview1(int rating, String comment) {
        return new Review.Builder()
                .setReviewRating(rating)
                .setReviewComment(comment)
                .build();
    }
}
