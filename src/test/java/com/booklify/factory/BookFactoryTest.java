package com.booklify.factory;

import com.booklify.domain.Book;
import com.booklify.domain.RegularUser;
import com.booklify.domain.enums.BookCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookFactoryTest {
    private Book book1;
    private RegularUser user;
    private byte[] image;

    @BeforeEach
    void setUp() {
        user = new RegularUser.RegularUserBuilder()
                .setFullName("Pelisa Pali")
                .setEmail("pelisa@example.com")
                .setPassword("securePassword123")
                .build();

        image = new byte[]{1, 2, 3}; // Simulating an image byte array
    }

    @Test
    void createBook_successfully() {
        book1 = BookFactory.createBook(
                "9783161484100",
                "Atomic Habits",
                "James Clear",
                "Penguin Random House",
                BookCondition.ACCEPTABLE,
                19.99,
                "Minor marks in pages but readable",
                image,
                user
        );

        assertNotNull(book1);
        assertEquals("Atomic Habits", book1.getTitle());
        assertEquals(BookCondition.ACCEPTABLE, book1.getCondition());
        assertEquals("Pelisa Pali", book1.getUser().getFullName());
        System.out.println(book1);
    }

    @Test
    void createBook_withNullISBN_shouldThrow() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                BookFactory.createBook(
                        null,
                        "Rich Dad Poor Dad",
                        "Robert Kiyosaki",
                        "Warner Books",
                        BookCondition.EXCELLENT,
                        29.99,
                        "A must-read financial book.",
                        image,
                        user
                )
        );

        assertEquals("Invalid ISBN.", exception.getMessage());
    }

    @Test
    void createBook_withDifferentPublisher_shouldSucceed() {
        Book book2 = BookFactory.createBook(
                "9781111111111",
                "Effective Java",
                "Joshua Bloch",
                "Addison-Wesley",
                BookCondition.ACCEPTABLE,
                45.00,
                "Gently used, no markings",
                image,
                user
        );
        System.out.println(book2);
    }
}