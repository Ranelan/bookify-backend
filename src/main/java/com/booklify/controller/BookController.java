package com.booklify.controller;
import com.booklify.domain.Book;

import com.booklify.domain.RegularUser;
import com.booklify.service.impl.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import com.booklify.dto.BookDto;
import com.booklify.repository.RegularUserRepository;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://127.0.0.1:3000")
@RestController
@RequestMapping("/api/book")
public class BookController {

    @Autowired
    private BookService service;

    @Autowired
    private RegularUserRepository regularUserRepository;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@RequestPart("bookRequest") BookDto bookDto,
                                    @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {

        System.out.println("BookController.create called. BookDto: " + bookDto);

        if (bookDto.getUserId() == null) {
            return ResponseEntity.badRequest().body("UploaderId is required.");
        }
        RegularUser user = regularUserRepository.findById(bookDto.getUserId())
                .orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        Book.Builder builder = new Book.Builder()
                .setBookID(bookDto.getBookID())
                .setIsbn(bookDto.getIsbn())
                .setTitle(bookDto.getTitle())
                .setAuthor(bookDto.getAuthor())
                .setPublisher(bookDto.getPublisher())
                .setCondition(bookDto.getCondition())
                .setPrice(bookDto.getPrice())
                .setDescription(bookDto.getDescription())
                .setUploadedDate(bookDto.getUploadedDate())
                .setUser(user) // Always set user
                .setAvailable(bookDto.getAvailable()); // Set available from DTO

        if (imageFile != null && !imageFile.isEmpty()) {
            builder.setImage(imageFile.getBytes());
        }
        Book savedBook = service.save(builder.build());
        return ResponseEntity.ok(BookDto.fromEntity(savedBook));
    }


    @GetMapping("/read/{id}")
    public ResponseEntity<BookDto> read(@PathVariable Long id) {
        Book book = service.findById(id);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(BookDto.fromEntity(book));
    }

    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BookDto> update(@RequestPart("bookRequest") BookDto bookDto,
                                          @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {

        System.out.println("Received BookDto: " + bookDto);
        Book.Builder builder = new Book.Builder()
                .setBookID(bookDto.getBookID())
                .setIsbn(bookDto.getIsbn())
                .setTitle(bookDto.getTitle())
                .setAuthor(bookDto.getAuthor())
                .setPublisher(bookDto.getPublisher())
                .setCondition(bookDto.getCondition())
                .setPrice(bookDto.getPrice())
                .setDescription(bookDto.getDescription())
                .setUploadedDate(bookDto.getUploadedDate());
        if (bookDto.getUserId() != null) {
            regularUserRepository.findById(bookDto.getUserId()).ifPresent(builder::setUser);
        }
        if (imageFile != null && !imageFile.isEmpty()) {
            builder.setImage(imageFile.getBytes());
        }
        Book updatedBook = service.update(builder.build());
        return ResponseEntity.ok(BookDto.fromEntity(updatedBook));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<BookDto>> getAll() {
        List<Book> books = service.getAll();
        List<BookDto> dtos = books.stream().map(BookDto::fromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getBookImage(@PathVariable Long id) {
        Book book = service.findById(id);
        if (book != null && book.getImage() != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(book.getImage(), headers, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<BookDto>> searchBooks(@RequestParam String query) {
        List<Book> books = service.findByTitleContainingIgnoreCase(query);
        if (books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<BookDto> dtos = books.stream().map(BookDto::fromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/search/author")
    public ResponseEntity<List<BookDto>> searchBooksByAuthor(@RequestParam String author) {
        List<Book> books = service.findByAuthor(author);
        if (books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<BookDto> dtos = books.stream().map(BookDto::fromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/search/isbn")
    public ResponseEntity<List<BookDto>> searchBookByIsbn(@RequestParam String isbn) {
        List<Book> books = service.findByIsbn(isbn);
        if (books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<BookDto> dtos = books.stream().map(BookDto::fromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookDto>> getBooksByUserId(@PathVariable Long userId) {
        List<Book> books = service.findByUserId(userId);
        if (books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<BookDto> dtos = books.stream().map(BookDto::fromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
