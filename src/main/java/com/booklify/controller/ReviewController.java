// Thaakirah Watson, 230037550
package com.booklify.controller;

import com.booklify.domain.Review;
import com.booklify.service.IReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final IReviewService reviewService;

    public ReviewController(IReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody Review review) {
        try {
            if (review == null) {
                return ResponseEntity.badRequest().body("Review data cannot be null");
            }

            // Validate required fields
            if (review.getReviewRating() < 1 || review.getReviewRating() > 5) {
                return ResponseEntity.badRequest().body("Rating must be between 1 and 5");
            }

            if (review.getReviewComment() == null || review.getReviewComment().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Review comment is required");
            }

            if (review.getUser() == null) {
                return ResponseEntity.badRequest().body("User is required");
            }

            if (review.getBook() == null) {
                return ResponseEntity.badRequest().body("Book is required");
            }

            // Set review date if not provided
            if (review.getReviewDate() == null) {
                review.setReviewDate(java.time.LocalDate.now());
            }

            Review createdReview = reviewService.save(review);
            return ResponseEntity.status(201).body(createdReview);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to create review: " + e.getMessage());
        }
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<?> read(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().body("Invalid review ID");
            }

            Review review = reviewService.findById(id);
            if (review != null) {
                return ResponseEntity.ok(review);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to retrieve review: " + e.getMessage());
        }
    }

    @PutMapping("/update/{reviewId}")
    public ResponseEntity<?> update(@PathVariable Long reviewId, @Valid @RequestBody Review review) {
        try {
            if (reviewId == null || reviewId <= 0) {
                return ResponseEntity.badRequest().body("Invalid review ID");
            }

            if (review == null) {
                return ResponseEntity.badRequest().body("Review data cannot be null");
            }

            // Validate required fields
            if (review.getReviewRating() < 1 || review.getReviewRating() > 5) {
                return ResponseEntity.badRequest().body("Rating must be between 1 and 5");
            }

            if (review.getReviewComment() == null || review.getReviewComment().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Review comment is required");
            }

            // Set the review ID from path variable
            review.setReviewId(reviewId);

            Review updatedReview = reviewService.update(review);
            if (updatedReview != null) {
                return ResponseEntity.ok(updatedReview);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to update review: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().body("Invalid review ID");
            }

            boolean deleted = reviewService.deleteById(id);
            if (deleted) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to delete review: " + e.getMessage());
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {
        try {
            List<Review> reviews = reviewService.getAll();
            if (reviews.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to retrieve reviews: " + e.getMessage());
        }
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<?> getReviewsByBook(@PathVariable Long bookId) {
        try {
            if (bookId == null || bookId <= 0) {
                return ResponseEntity.badRequest().body("Invalid book ID");
            }

            List<Review> reviews = reviewService.getReviewsByBook(bookId);
            if (reviews.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to retrieve reviews for book: " + e.getMessage());
        }
    }

    @GetMapping("/check/{bookId}/{userId}")
    public ResponseEntity<?> checkUserReviewForBook(@PathVariable Long bookId, @PathVariable Long userId) {
        try {
            if (bookId == null || bookId <= 0) {
                return ResponseEntity.badRequest().body("Invalid book ID");
            }

            if (userId == null || userId <= 0) {
                return ResponseEntity.badRequest().body("Invalid user ID");
            }

            List<Review> reviews = reviewService.getReviewsByBook(bookId);
            Review userReview = reviews.stream()
                    .filter(review -> review.getUser() != null && review.getUser().getId().equals(userId))
                    .findFirst()
                    .orElse(null);

            if (userReview != null) {
                return ResponseEntity.ok(userReview);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to check user review: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getReviewsByUser(@PathVariable Long userId) {
        try {
            if (userId == null || userId <= 0) {
                return ResponseEntity.badRequest().body("Invalid user ID");
            }

            List<Review> reviews = reviewService.getReviewsByRegularUser(userId);
            if (reviews.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to retrieve reviews for user: " + e.getMessage());
        }
    }
}
