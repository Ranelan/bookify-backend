// Thaakirah Watson, 230037550
package com.booklify.service.impl;

import com.booklify.domain.Book;
import com.booklify.domain.RegularUser;
import com.booklify.domain.Review;
import com.booklify.repository.BookRepository;
import com.booklify.repository.RegularUserRepository;
import com.booklify.repository.ReviewRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReviewServiceTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RegularUserRepository regularUserRepository;

    @Autowired
    private BookRepository bookRepository;

    private Review review;
    private RegularUser user;
    private Book book;

    @BeforeEach
    void setUp() {
        // Ensure clean slate
        reviewRepository.deleteAll();
        bookRepository.deleteAll();
        regularUserRepository.deleteAll();

        // Create and save a user
        user = new RegularUser.RegularUserBuilder()
                .setFullName("Service Test User")
                .setEmail("serviceuser@example.com")
                .setPassword("pass123")
                .setDateJoined(LocalDateTime.now())
                .build();
        user = regularUserRepository.save(user);

        // Create and save a book
        book = new Book.Builder()
                .setTitle("Service Test Book")
                .setAuthor("Author S")
                .setIsbn("1122334455")
                .setUploadedDate(LocalDateTime.now())
                .build();
        book = bookRepository.save(book);

        // Create and save a review
        review = new Review.Builder()
                .setReviewRating(4)
                .setReviewComment("Good book")
                .setReviewDate(LocalDate.now())
                .setUser(user)
                .setBook(book)
                .build();
        review = reviewService.save(review);
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    void save() {
        Review newReview = new Review.Builder()
                .setReviewRating(5)
                .setReviewComment("Excellent read!")
                .setReviewDate(LocalDate.now())
                .setUser(user)
                .setBook(book)
                .build();

        Review saved = reviewService.save(newReview);
        assertNotNull(saved.getReviewId(), "Saved Review ID should not be null");
        assertEquals("Excellent read!", saved.getReviewComment(), "Saved comment should match input");
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    void findById() {
        Review found = reviewService.findById(review.getReviewId());
        assertNotNull(found, "Review should be found by ID");
        assertEquals(review.getReviewId(), found.getReviewId(), "Found review ID should match saved one");
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    void update() {
        Review updated = new Review.Builder()
                .copy(review)
                .setReviewComment("Updated review comment")
                .setReviewRating(3)
                .build();

        Review saved = reviewService.update(updated);
        assertNotNull(saved, "Updated Review should not be null");
        assertEquals("Updated review comment", saved.getReviewComment(), "Comment should be updated");
        assertEquals(3, saved.getReviewRating(), "Rating should be updated");
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    void deleteById() {
        boolean deleted = reviewService.deleteById(review.getReviewId());
        assertTrue(deleted, "Review should be deleted successfully");
        assertFalse(reviewRepository.findById(review.getReviewId()).isPresent(), "Deleted review should not exist in repository");
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    void getAll() {
        List<Review> allReviews = reviewService.getAll();
        assertFalse(allReviews.isEmpty(), "getAll should return at least one review");
        assertTrue(allReviews.stream().anyMatch(r -> r.getReviewId().equals(review.getReviewId())),
                "List should contain the saved review");
    }

    @Test
    @org.junit.jupiter.api.Order(6)
    void getReviewsByRegularUser() {
        List<Review> userReviews = reviewService.getReviewsByRegularUser(user.getId());
        assertFalse(userReviews.isEmpty(), "Reviews by user should not be empty");
        assertEquals(user.getId(), userReviews.get(0).getUser().getId(), "User ID should match");
    }

    @Test
    @org.junit.jupiter.api.Order(7)
    void getReviewsByBook() {
        List<Review> bookReviews = reviewService.getReviewsByBook(book.getBookID());
        assertFalse(bookReviews.isEmpty(), "Reviews by book should not be empty");
        assertEquals(book.getBookID(), bookReviews.get(0).getBook().getBookID(), "Book ID should match");
    }
}
