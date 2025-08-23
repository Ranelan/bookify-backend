package com.booklify.controller;

import com.booklify.domain.Admin;
import com.booklify.domain.RegularUser;
import com.booklify.dto.AdminDto;
import com.booklify.dto.BookDto;
import com.booklify.dto.RegularUserDto;
import com.booklify.service.impl.AdminService;
import com.booklify.util.JwtUtil;
import com.booklify.util.RegularUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.booklify.repository.RegularUserRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RegularUserRepository regularUserRepository;

    @PostMapping("/create")
    public ResponseEntity<AdminDto> createAdmin(@RequestBody AdminDto adminDto) {
        Admin admin = AdminDto.toEntity(adminDto);
        Admin saved = adminService.save(admin);
        return ResponseEntity.ok(AdminDto.fromEntity(saved));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody RegularUserController.LoginRequest request) {
        Admin admin = adminService.login(request.getEmail(), request.getPassword());
        String token = jwtUtil.generateToken(admin.getEmail());
        return ResponseEntity.ok(token);
    }


    @GetMapping("/getById/{id}")
    public ResponseEntity<AdminDto> getAdmin(@PathVariable Long id) {
        Admin admin = adminService.findById(id);
        return ResponseEntity.ok(AdminDto.fromEntity(admin));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AdminDto> updateAdmin(@PathVariable Long id, @RequestBody AdminDto adminDto) {
        Admin existing = adminService.findById(id);

        Admin updated = new Admin.AdminBuilder()
                .copy(existing)
                .setFullName(adminDto.getFullName())
                .setEmail(adminDto.getEmail())
                .setPassword(adminDto.getPassword())
                .setLastLogin(adminDto.getLastLogin())
                .setDateJoined(adminDto.getDateJoined())
                .setPermissions(adminDto.getPermissions())
                .build();

        Admin saved = adminService.update(updated);
        return ResponseEntity.ok(AdminDto.fromEntity(saved));
    }

    @DeleteMapping("/deleteAdmin/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        adminService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/all")
    public ResponseEntity<List<RegularUserDto>> viewAllRegularUsers() {
        List<RegularUser> users = adminService.viewAllRegularUsers();
        List<RegularUserDto> dtos = users.stream()
                .map(RegularUserMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteRegularUser(@PathVariable Long id) {
        adminService.deleteRegularUserById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<Void> updateRegularUser(@PathVariable Long id, @RequestBody RegularUserDto userDto) {
        RegularUser updated = RegularUserMapper.toEntity(userDto);
        adminService.updateRegularUserById(id, updated);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/email")
    public ResponseEntity<List<RegularUserDto>> searchByEmail(@RequestParam String email) {
        List<RegularUser> users = adminService.findAllRegularUsersByEmail(email);
        List<RegularUserDto> dtos = users.stream()
                .map(RegularUserMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/users/name")
    public ResponseEntity<List<RegularUserDto>> searchByName(@RequestParam String fullName) {
        List<RegularUser> users = adminService.findAllRegularUsersByFullName(fullName);
        List<RegularUserDto> dtos = users.stream()
                .map(RegularUserMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // --- Book Management Endpoints ---

    @GetMapping("/getAllBooks")
    public ResponseEntity<List<BookDto>> viewAllBookListings() {
        return ResponseEntity.ok(adminService.viewAllBookListings().stream().map(BookDto::fromEntity).toList());
    }

    @DeleteMapping("/deleteBook/{bookId}")
    public ResponseEntity<Void> deleteBookListingById(@PathVariable Long bookId) {
        adminService.deleteBookListingById(bookId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/editBook/{bookId}")
    public ResponseEntity<BookDto> editBookListingById(@PathVariable Long bookId, @RequestBody BookDto updatedListing) {
        // Convert DTO to entity, set user if uploaderId is present
        var builder = new com.booklify.domain.Book.Builder()
                .setBookID(updatedListing.getBookID())
                .setIsbn(updatedListing.getIsbn())
                .setTitle(updatedListing.getTitle())
                .setAuthor(updatedListing.getAuthor())
                .setPublisher(updatedListing.getPublisher())
                .setCondition(updatedListing.getCondition())
                .setPrice(updatedListing.getPrice())
                .setDescription(updatedListing.getDescription())
                .setUploadedDate(updatedListing.getUploadedDate())
                .setImage(updatedListing.getImage());
        if (updatedListing.getUserId() != null) {
            regularUserRepository.findById(updatedListing.getUserId()).ifPresent(builder::setUser);
        }
        com.booklify.domain.Book bookEntity = builder.build();
        adminService.editBookListingById(bookId, bookEntity);
        // Return the updated book as DTO
        return ResponseEntity.ok(BookDto.fromEntity(bookEntity));
    }

    @GetMapping("/books/{bookId}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long bookId) {
        return ResponseEntity.ok(BookDto.fromEntity(adminService.getBookById(bookId)));
    }

    @GetMapping("/books/search/title")
    public ResponseEntity<List<BookDto>> searchBooksByTitle(@RequestParam String title) {
        return ResponseEntity.ok(adminService.searchBooksByTitle(title).stream().map(BookDto::fromEntity).toList());
    }

    @GetMapping("/books/search/author")
    public ResponseEntity<List<BookDto>> searchBooksByAuthor(@RequestParam String author) {
        return ResponseEntity.ok(adminService.searchBooksByAuthor(author).stream().map(BookDto::fromEntity).toList());
    }

    @GetMapping("/books/search/isbn")
    public ResponseEntity<List<BookDto>> searchBooksByIsbn(@RequestParam String isbn) {
        return ResponseEntity.ok(adminService.searchBooksByIsbn(isbn).stream().map(BookDto::fromEntity).toList());
    }

    @GetMapping("/books/user/{userId}")
    public ResponseEntity<List<BookDto>> findBooksByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.findBooksByUserId(userId).stream().map(BookDto::fromEntity).toList());
    }


}
