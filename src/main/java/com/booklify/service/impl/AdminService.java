package com.booklify.service.impl;

import com.booklify.domain.*;
//import com.booklify.domain.Book;
import com.booklify.domain.enums.OrderStatus;
import com.booklify.domain.enums.Permissions;
import com.booklify.repository.*;
//import com.booklify.repository.BookRepository;
import com.booklify.service.IAdminService;
import com.booklify.util.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService implements IAdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RegularUserRepository regularUserRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Admin save(Admin admin) {
        String encodedPassword = admin.getPassword() != null && !admin.getPassword().isBlank()
                ? passwordEncoder.encode(admin.getPassword())
                : null;

        List<Permissions> permissions = (admin.getPermissions() == null || admin.getPermissions().isEmpty())
                ? Arrays.asList(Permissions.values())
                : admin.getPermissions();

        Admin savedAdmin = new Admin.AdminBuilder()
                .setId(admin.getId())
                .setFullName(admin.getFullName())
                .setEmail(admin.getEmail())
                .setPassword(encodedPassword)
                .setPermissions(permissions)
                .setDateJoined(admin.getDateJoined())
                .setLastLogin(admin.getLastLogin())
                .build();

        return adminRepository.save(savedAdmin);
    }

    @Override
    public Admin findById(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + id));
    }

    @Override
    public Admin update(Admin admin) {
        Admin existing = findById(admin.getId());

        Admin updated = new Admin.AdminBuilder()
                .copy(existing)
                .setFullName(admin.getFullName())
                .setEmail(admin.getEmail())
                .setPermissions(existing.getPermissions())
                .setDateJoined(existing.getDateJoined())
                .setLastLogin(existing.getLastLogin())
                .build();

        if (admin.getPassword() != null && !admin.getPassword().isBlank()) {
            updated.setPassword(passwordEncoder.encode(admin.getPassword()));
        } else {
            updated.setPassword(existing.getPassword());
        }

        return adminRepository.save(updated);
    }

    @Override
    public boolean deleteById(Long id) {
        adminRepository.deleteById(id);
        return false;
    }

    @Override
    public Optional<Admin> findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    @Override
    public List<Admin> findByFullName(String fullName) {
        return adminRepository.findByFullName(fullName);
    }

    @Override
    public List<RegularUser> findAllRegularUsersByEmail(String email) {
        return regularUserRepository.findByEmail(email)
                .map(List::of)
                .orElse(List.of());
    }

    @Override
    public List<RegularUser> findAllRegularUsersByFullName(String fullName) {
        return regularUserRepository.findByFullName(fullName);
    }

    @Override
    public Admin login(String email, String password) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        Admin updated = new Admin.AdminBuilder()
                .copy(admin)
                .setLastLogin(LocalDateTime.now())
                .build();

        adminRepository.save(updated);

        return updated;
    }


    // Regular User Management Methods
    @Override
    public List<RegularUser> viewAllRegularUsers() {
        return regularUserRepository.findAll();
    }

    @Override
    public void deleteRegularUserById(Long id) {
        regularUserRepository.deleteById(id);
    }

    @Override
    public void updateRegularUserById(Long id, RegularUser updatedUser) {
        RegularUser user = regularUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFullName(updatedUser.getFullName());
        user.setEmail(updatedUser.getEmail());
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        regularUserRepository.save(user);
    }

    // Book Management Methods
    @Override
    public List<Book> viewAllBookListings() {
        return bookRepository.findAll();
    }

    @Override
    public void deleteBookListingById(Long bookId) {
        bookRepository.deleteById(bookId);
    }

    @Override
    public void editBookListingById(Long bookId, Book updatedListing) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));
        Book updated = new Book.Builder()
                .copy(book)
                .setTitle(updatedListing.getTitle())
                .setAuthor(updatedListing.getAuthor())
                .setCondition(updatedListing.getCondition())
                .setPrice(updatedListing.getPrice())
                .setDescription(updatedListing.getDescription())
                .setIsbn(updatedListing.getIsbn())
                .setPublisher(updatedListing.getPublisher())
                .setUploadedDate(updatedListing.getUploadedDate())
                .setUser(book.getUser()) // Always retain the original user
                .build();
        bookRepository.save(updated);
    }

    @Override
    public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));
    }

    @Override
    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public List<Book> searchBooksByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }

    @Override
    public List<Book> searchBooksByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    @Override
    public List<Book> findBooksByUserId(Long userId) {
        return bookRepository.findAll().stream()
                .filter(book -> book.getUser() != null && book.getUser().getId().equals(userId))
                .toList();
    }


    // Order Management Methods
    @Override
    public List<Order> viewAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional
    public void updateOrderItemStatus(Long orderItemId, String newStatus) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new RuntimeException("OrderItem not found with id: " + orderItemId));
        try {
            OrderStatus statusEnum = OrderStatus.valueOf(newStatus.toUpperCase());
            orderItem.setOrderStatus(statusEnum);
            orderItemRepository.save(orderItem);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid order status: " + newStatus);
        }
    }

    @Override
    public List<OrderItem> viewAllOrderItems() {
        return orderItemRepository.findAll();
    }

    @Override
    public List<Order> searchOrdersByUserId(Long userId) {
        return orderRepository.findByRegularUserId(userId);
    }

    @Override
    public List<OrderItem> searchOrderItemsByOrderId(Long orderId) {
        return orderItemRepository.findByOrder_OrderId(orderId);
    }

    @Override
    public List<OrderItem> searchOrderItemsByStatus(String status) {
        try {
            com.booklify.domain.enums.OrderStatus orderStatus = com.booklify.domain.enums.OrderStatus.valueOf(status);
            return orderItemRepository.findByOrderStatus(orderStatus);
        } catch (IllegalArgumentException e) {
            return List.of();
        }
    }

    @Override
    public Double calculateTotalRevenue() {
        // The logic is now simpler: just call the repository method that sums everything.
        return orderItemRepository.sumTotalAmountOfAllItems();
    }

    @Override
    public Double calculateRevenueByDateRange(String startDate, String endDate) {
        // Date parsing remains the same.
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);

        // The repository call is now simpler and does not filter by status.
        return orderItemRepository.sumTotalAmountByOrderDateBetween(start, end);
    }

}
