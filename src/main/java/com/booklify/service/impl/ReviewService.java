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
        return reviewRepository.save(entity);
    }

    @Override
    public Review findById(Long aLong) {
        return reviewRepository.findById(aLong)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + aLong));
    }

    @Override
    public Review update(Review entity) {
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
        return reviewRepository.findByUserId(id);
    }

    @Override
    public List<Review> getReviewsByBook(Long bookId) {
        return reviewRepository.findByBook_BookID(bookId);
    }

}
