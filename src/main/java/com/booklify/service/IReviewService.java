// Thaakirah Watson, 230037550
package com.booklify.service;

import com.booklify.domain.Review;

import java.util.List;

public interface IReviewService extends IService<Review, Long> {

    List<Review> getAll();
    List<Review> getReviewsByRegularUser(Long id);
    List<Review> getReviewsByBook(Long bookId);
}
