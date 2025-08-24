// Thaakirah Watson, 230037550
package com.booklify.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    private int reviewRating;
    private String reviewComment;
    private LocalDate reviewDate;

    // 🔗 Relationship: Many reviews can be written by one user
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private RegularUser user;

    // 🔗 Relationship: Many reviews belong to one book
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    protected Review() {
        // required by JPA
    }

    private Review(Builder builder) {
        this.reviewId = builder.reviewId;
        this.reviewRating = builder.reviewRating;
        this.reviewComment = builder.reviewComment;
        this.reviewDate = builder.reviewDate;
        this.user = builder.user;
        this.book = builder.book;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public int getReviewRating() {
        return reviewRating;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public User getUser() {
        return user;
    }

    public Book getBook() {
        return book;
    }

    public static class Builder {
        private Long reviewId;
        private int reviewRating;
        private String reviewComment;
        private LocalDate reviewDate;
        private RegularUser user;
        private Book book;

        public Builder setReviewId(Long reviewId) {
            this.reviewId = reviewId;
            return this;
        }

        public Builder setReviewRating(int reviewRating) {
            this.reviewRating = reviewRating;
            return this;
        }

        public Builder setReviewComment(String reviewComment) {
            this.reviewComment = reviewComment;
            return this;
        }

        public Builder setReviewDate(LocalDate reviewDate) {
            this.reviewDate = reviewDate;
            return this;
        }

        public Builder setUser(RegularUser user) {
            this.user = user;
            return this;
        }

        public Builder setBook(Book book) {
            this.book = book;
            return this;
        }

        public Builder copy(Review review) {
            this.reviewId = review.reviewId;
            this.reviewRating = review.reviewRating;
            this.reviewComment = review.reviewComment;
            this.reviewDate = review.reviewDate;
            this.user = review.user;
            this.book = review.book;
            return this;
        }

        public Review build() {
            return new Review(this);
        }
    }
}