// Thaakirah Watson, 230037550
package com.booklify.repository;

import com.booklify.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByBook_BookID(Long bookID);
    List<Review> findByUserId(Long id);

}
