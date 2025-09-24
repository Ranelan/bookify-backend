//// Thaakirah Watson, 230037550
//package com.booklify.controller;
//
//import com.booklify.domain.Book;
//import com.booklify.domain.RegularUser;
//import com.booklify.domain.Review;
//import com.booklify.repository.BookRepository;
//import com.booklify.repository.RegularUserRepository;
//import com.booklify.service.impl.ReviewService;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.ResponseEntity;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class ReviewControllerTest {
//
//    @Autowired
//    private ReviewController reviewController;
//
//    @Autowired
//    private ReviewService reviewService;
//
//    @Autowired
//    private RegularUserRepository regularUserRepository;
//
//    @Autowired
//    private BookRepository bookRepository;
//
//    private Review review;
//    private RegularUser user;
//    private Book book;
//
//    @BeforeEach
//    void setUp() {
//        // Create and save a user
//        user = new RegularUser.RegularUserBuilder()
//                .setFullName("Jane Doe")
//                .setEmail("jane@example.com")
//                .setPassword("secure123")
//                .setDateJoined(LocalDateTime.now())
//                .build();
//        user = regularUserRepository.save(user);
//
//        // Create and save a book
//        book = new Book.Builder()
//                .setTitle("JUnit in Action")
//                .setAuthor("Test Author")
//                .setIsbn("111222333")
//                .setUploadedDate(LocalDateTime.now())
//                .build();
//        book = bookRepository.save(book);
//
//        // Create and save a review
//        review = new Review.Builder()
//                .setReviewRating(5)
//                .setReviewComment("Great testing book!")
//                .setReviewDate(LocalDate.now())
//                .setUser(user)
//                .setBook(book)
//                .build();
//
//        review = reviewService.save(review);
//    }
//
//    @Test
//    @org.junit.jupiter.api.Order(1)
//    void create() {
//        Review newReview = new Review.Builder()
//                .setReviewRating(4)
//                .setReviewComment("Very helpful resource")
//                .setReviewDate(LocalDate.now())
//                .setUser(user)
//                .setBook(book)
//                .build();
//
//        ResponseEntity<Review> response = reviewController.create(newReview);
//        assertEquals(201, response.getStatusCodeValue(), "Create should return 201");
//        assertNotNull(response.getBody().getReviewId(), "Saved Review ID should not be null");
//    }
//
//    @Test
//    @org.junit.jupiter.api.Order(2)
//    void read() {
//        ResponseEntity<Review> response = reviewController.read(review.getReviewId());
//        assertEquals(200, response.getStatusCodeValue(), "Read should return 200");
//        assertEquals(review.getReviewId(), response.getBody().getReviewId(), "Review ID should match");
//    }
//
//    @Test
//    @org.junit.jupiter.api.Order(3)
//    void update() {
//        Review updated = new Review.Builder()
//                .copy(review)
//                .setReviewComment("Updated comment")
//                .build();
//
//        ResponseEntity<Review> response = reviewController.update(updated);
//        assertEquals(200, response.getStatusCodeValue(), "Update should return 200");
//        assertEquals("Updated comment", response.getBody().getReviewComment(), "Comment should be updated");
//    }
//
//    @Test
//    @org.junit.jupiter.api.Order(4)
//    void delete() {
//        ResponseEntity<Void> response = reviewController.delete(review.getReviewId());
//        assertEquals(204, response.getStatusCodeValue(), "Delete should return 204");
//    }
//
//    @Test
//    @org.junit.jupiter.api.Order(5)
//    void getAll() {
//        ResponseEntity<List<Review>> response = reviewController.getAll();
//        assertEquals(200, response.getStatusCodeValue(), "GetAll should return 200 if reviews exist");
//        assertFalse(response.getBody().isEmpty(), "GetAll should not return an empty list");
//    }
//
//    @Test
//    @org.junit.jupiter.api.Order(6)
//    void getReviewsByBook() {
//        ResponseEntity<List<Review>> response = reviewController.getReviewsByBook(book.getBookID());
//        assertEquals(200, response.getStatusCodeValue(), "getReviewsByBook should return 200");
//        assertTrue(response.getBody().stream().anyMatch(r -> r.getBook().getBookID().equals(book.getBookID())),
//                "Response should include reviews for the given book");
//    }
//
//    @Test
//    @org.junit.jupiter.api.Order(7)
//    void getReviewsByUser() {
//        ResponseEntity<List<Review>> response = reviewController.getReviewsByUser(user.getId());
//        assertEquals(200, response.getStatusCodeValue(), "getReviewsByUser should return 200");
//        assertTrue(response.getBody().stream().anyMatch(r -> r.getUser().getId().equals(user.getId())),
//                "Response should include reviews by the given user");
//    }
//}
