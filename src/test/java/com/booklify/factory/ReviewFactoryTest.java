// Thaakirah Watson, 230037550
package com.booklify.factory;

import com.booklify.domain.Book;
import com.booklify.domain.RegularUser;
import com.booklify.domain.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReviewFactoryTest {

    private RegularUser user;
    private Book book;

    @BeforeEach
    void setUp() {
        user = new RegularUser.RegularUserBuilder()
                .setId(1L)
                .setFullName("Test User")
                .setEmail("test@example.com")
                .setPassword("password123")
                .setDateJoined(LocalDateTime.now())
                .build();

        book = new Book.Builder()
                .setBookID(1L)
                .setTitle("Factory Testing")
                .setAuthor("Author X")
                .setIsbn("987654321")
                .setUploadedDate(LocalDateTime.now())
                .build();
    }

    @Test
    void getInstance() {
        ReviewFactory instance1 = ReviewFactory.getInstance();
        ReviewFactory instance2 = ReviewFactory.getInstance();
        assertNotNull(instance1, "ReviewFactory instance should not be null");
        assertSame(instance1, instance2, "ReviewFactory should follow singleton pattern");
    }

    @Test
    void createReview() {
        Review review = ReviewFactory.createReview(
                5,
                "Excellent book!",
                LocalDate.now(),
                user,
                book
        );

        assertNotNull(review, "Created review should not be null");
        assertEquals(5, review.getReviewRating(), "Review rating should match input");
        assertEquals("Excellent book!", review.getReviewComment(), "Review comment should match input");
        assertEquals(user.getId(), review.getUser().getId(), "User ID should match input user");
        assertEquals(book.getBookID(), review.getBook().getBookID(), "Book ID should match input book");
    }

    @Test
    void createReview1() {
        Review review = ReviewFactory.createReview1(3, "Good read");

        assertNotNull(review, "Created review should not be null");
        assertEquals(3, review.getReviewRating(), "Review rating should match input");
        assertEquals("Good read", review.getReviewComment(), "Review comment should match input");
        assertNull(review.getUser(), "User should be null when not provided");
        assertNull(review.getBook(), "Book should be null when not provided");
    }
}
