package com.booklify.repository;

import com.booklify.domain.Book;
import com.booklify.domain.enums.BookCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {


    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByAuthor(String author);

    List<Book> findByCondition(BookCondition condition);

    List<Book> findByPriceLessThanEqual(Double maxPrice);

    List<Book> findByIsbn(String isbn);

    List<Book> findByUserId(Long userId);

    @Query("SELECT b FROM Book b WHERE b.price > :minPrice AND b.condition = :condition")
    List<Book> findBooksByMinPriceAndCondition(@Param("minPrice") Double minPrice, @Param("condition") BookCondition condition);
}

