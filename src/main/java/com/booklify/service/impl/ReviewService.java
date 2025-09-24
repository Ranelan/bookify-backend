package com.booklify.service.impl;

import com.booklify.domain.Review;
import com.booklify.repository.ReviewRepository;
import com.booklify.service.IReviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService implements IReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }


    @Override
    public Review save(Review entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Review entity cannot be null");
        }
        if (entity.getReviewRating() < 1 || entity.getReviewRating() > 5) {
            throw new IllegalArgumentException("Review rating must be between 1 and 5");
        }
        if (entity.getReviewComment() == null || entity.getReviewComment().trim().isEmpty()) {
            throw new IllegalArgumentException("Review comment cannot be null or empty");
        }
        if (entity.getUser() == null) {
            throw new IllegalArgumentException("Review user cannot be null");
        }
        if (entity.getBook() == null) {
            throw new IllegalArgumentException("Review book cannot be null");
        }

        return reviewRepository.save(entity);
    }

    @Override
    public Review findById(Long aLong) {
        return reviewRepository.findById(aLong)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + aLong));
    }

    @Override
    public Review update(Review entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Review entity cannot be null");
        }
        if (entity.getReviewId() == null) {
            throw new IllegalArgumentException("Review ID cannot be null for update");
        }
        if (entity.getReviewRating() < 1 || entity.getReviewRating() > 5) {
            throw new IllegalArgumentException("Review rating must be between 1 and 5");
        }
        if (entity.getReviewComment() == null || entity.getReviewComment().trim().isEmpty()) {
            throw new IllegalArgumentException("Review comment cannot be null or empty");
        }

        Review existing = findById(entity.getReviewId());

        Review updatedReview = new Review.Builder()
                .copy(existing)
                .setReviewRating(entity.getReviewRating())
                .setReviewComment(entity.getReviewComment())
                .setReviewDate(entity.getReviewDate())
                .setUser((com.booklify.domain.RegularUser) entity.getUser())
                .setBook(entity.getBook())
                .build();

        return reviewRepository.save(updatedReview);
    }

    @Override
    public boolean deleteById(Long aLong) {
        return reviewRepository.findById(aLong)
                .map(review -> {
                    reviewRepository.delete(review);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public List<Review> getAll() {
        return reviewRepository.findAll();
    }

    @Override
    public List<Review> getReviewsByRegularUser(Long id) {
        return reviewRepository.findByUser_Id(id);
    }

    @Override
    public List<Review> getReviewsByBook(Long bookId) {
        return reviewRepository.findByBook_BookID(bookId);
    }

}
