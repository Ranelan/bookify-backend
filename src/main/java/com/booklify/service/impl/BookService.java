package com.booklify.service.impl;
import com.booklify.domain.Book;
import com.booklify.repository.BookRepository;
import com.booklify.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService implements IBookService {
    @Autowired
    private BookRepository bookRepo;

    @Override
    public Book save(Book entity) {
        return bookRepo.save(entity);
    }

    @Override
    public Book findById(Long id) {
        Optional<Book> optional = bookRepo.findById(id);
        return optional.orElse(null);
    }

    @Override
    public Book update(Book entity) {
        return bookRepo.save(entity);
    }

    @Override
    public boolean deleteById(Long aLong) {
        if (bookRepo.existsById(aLong)) {
            bookRepo.deleteById(aLong);
        }
        return false;
    }

    @Override
    public List<Book> getAll() {
        return bookRepo.findAll();
    }

    @Override
    public void deleteAll() {
        bookRepo.deleteAll();
    }

    @Override
    public List<Book> findByIsbn(String isbn) {
        return bookRepo.findByIsbn(isbn);
    }

    @Override
    public List<Book> findByTitleContainingIgnoreCase(String title) {
        return bookRepo.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return bookRepo.findByAuthor(author);
    }

    @Override
    public List<Book> findByUserId(Long userId) {
        return bookRepo.findByUserId(userId);
    }
}

