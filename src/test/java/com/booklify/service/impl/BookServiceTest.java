package com.booklify.service.impl;
import com.booklify.domain.Book;
import com.booklify.domain.RegularUser;
import com.booklify.domain.enums.BookCondition;
import com.booklify.factory.BookFactory;
import com.booklify.repository.OrderItemRepository;
import com.booklify.repository.RegularUserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private RegularUserRepository regularUserRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    private Book book1, book2;
    private ByteArrayOutputStream outputStream;
    private BufferedImage image;
    private byte[] imageBytes;
    private RegularUser user;

    @BeforeEach
    void setUp() {
        String url = "C:\\Users\\raney\\Downloads\\cput log.png";
        // String url = "C:\\Users\\raney\\Downloads\\cput log.png.png";

        try {
            image = ImageIO.read(new File(url));
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outputStream);
            imageBytes = outputStream.toByteArray();
        } catch (IOException ex) {
            System.out.println("Image read error: " + ex.getMessage());
            imageBytes = new byte[]{1, 2, 3}; // fallback dummy image
        }

        // Ensure no duplicate user by email
        regularUserRepository.findAll().stream()
                .filter(u -> u.getEmail().equals("test@example.com"))
                .forEach(u -> {
                    // Delete all books for this user first
                    bookService.getAll().stream()
                            .filter(b -> b.getUser().getId().equals(u.getId()))
                            .forEach(b -> bookService.deleteById(b.getBookID()));
                    // Now delete the user
                    regularUserRepository.deleteById(u.getId());
                });

        user = new RegularUser.RegularUserBuilder()
                .setFullName("Test Seller")
                .setEmail("test@example.com")
                .setPassword("test123")
                .build();
        user = regularUserRepository.save(user);

        book1 = BookFactory.createBook(
                "9780061122415",
                "The Alchemist",
                "Paulo Coelho",
                "HarperOne",
                BookCondition.EXCELLENT,
                180.0,
                "Fiction classic about destiny.",
                imageBytes,
                user
        );

        book2 = BookFactory.createBook(
                "9780451524935",
                "1984",
                "George Orwell",
                "Secker & Warburg",
                BookCondition.ACCEPTABLE,
                150.0,
                "Dystopian novel set in totalitarian regime.",
                imageBytes,
                user
        );

//         System.out.println("Book 1: " + book1);
//         System.out.println("Book 2: " + book2);
    }

    @Test
    @Order(1)
    void save() {
        Book saved1 = bookService.save(book1);
        Book saved2 = bookService.save(book2);

        assertNotNull(saved1);
        assertNotNull(saved2);
        System.out.println("Saved: " + saved1);
    }

    @Test
    @Order(2)
    void findById() {
        Book saved = bookService.save(book1);
        Book found = bookService.findById(saved.getBookID());

        assertNotNull(found);
        assertEquals(saved.getBookID(), found.getBookID());
        System.out.println("Found: " + found);
    }

    @Test
    @Order(3)
    void update() {
        Book saved = bookService.save(book1);
        Book updated = new Book.Builder().copy(saved)
                .setPrice(199.99)
                .build();
        Book result = bookService.update(updated);

        assertNotNull(result);
        assertEquals(199.99, result.getPrice());
        System.out.println("Updated: " + result);
    }

    @Test
    @Order(4)
    void deleteById() {
        Book saved = bookService.save(book2);
        bookService.deleteById(saved.getBookID());

        Book deleted = bookService.findById(saved.getBookID());
        assertNull(deleted);
        System.out.println("Deleted book with ID: " + saved.getBookID());
    }

    @Test
    @Order(5)
    void getAll() {
        bookService.save(book1);
        bookService.save(book2);

        assertFalse(bookService.getAll().isEmpty());
        System.out.println("All books: " + bookService.getAll());
    }

    @Test
    @Order(6)
    void deleteAll() {
        // Clean up dependent order items first to avoid foreign key constraint errors
        orderItemRepository.deleteAll();
        bookService.save(book1);
        bookService.save(book2);
        bookService.deleteAll();

        assertTrue(bookService.getAll().isEmpty());
        System.out.println("All books deleted");
    }

    @Test
    @Order(7)
    void findByIsbn() {
        bookService.save(book1);
        List<Book> found = bookService.findByIsbn(book1.getIsbn());
        assertFalse(found.isEmpty());
        assertTrue(found.stream().anyMatch(b -> b.getIsbn().equals(book1.getIsbn())));
        System.out.println("Found by ISBN: " + found);
    }

    @Test
    @Order(8)
    void findByTitleContainingIgnoreCase() {
        bookService.save(book1);
        List<Book> found = bookService.findByTitleContainingIgnoreCase("alchemist");
        assertFalse(found.isEmpty());
        assertTrue(found.stream().anyMatch(b -> b.getTitle().equalsIgnoreCase(book1.getTitle())));
        System.out.println("Found by title (ignore case): " + found);
    }

    @Test
    @Order(9)
    void findByAuthor() {
        bookService.save(book2);
        List<Book> found = bookService.findByAuthor(book2.getAuthor());
        assertFalse(found.isEmpty());
        assertTrue(found.stream().anyMatch(b -> b.getAuthor().equals(book2.getAuthor())));
        System.out.println("Found by author: " + found);
    }
}
