package com.booklify.service;

import com.booklify.domain.*;
import com.booklify.dto.AdminDto;


import java.util.List;
import java.util.Optional;

public interface IAdminService extends IService<Admin, Long> {

    Optional<Admin> findByEmail(String email);
    List<Admin> findByFullName(String fullName);

    // Finding Regular Users by Email and Name
    List<RegularUser> findAllRegularUsersByEmail(String email);
    List<RegularUser> findAllRegularUsersByFullName(String fullName);

    // Logging in Admin
    Admin login(String email, String password);

    // Regular User Management
    List<RegularUser> viewAllRegularUsers();
    void deleteRegularUserById(Long id);
    void updateRegularUserById(Long id, RegularUser regularUser);



    // Book Listing Management to be implemented in the future when the Book class is defined
    List<Book> viewAllBookListings();
    void deleteBookListingById(Long bookId);
    void editBookListingById(Long bookId, Book updatedListing);
    Book getBookById(Long bookId);
    List<Book> searchBooksByTitle(String title);
    List<Book> searchBooksByAuthor(String author);
    List<Book> searchBooksByIsbn(String isbn);
    List<Book> findBooksByUserId(Long userId);


    //Order Management
    List<Order> viewAllOrders();
    void updateOrderItemStatus(Long orderItemId, String newStatus);
    List<OrderItem> viewAllOrderItems();
    List<Order> searchOrdersByUserId(Long userId);
    List<OrderItem> searchOrderItemsByOrderId(Long orderId);
    List<OrderItem> searchOrderItemsByStatus(String status);

    //Revanue Management
    Double calculateTotalRevenue();
    Double calculateRevenueByDateRange(String startDate, String endDate);



    // Placeholder methods for future implementation for the reports
//    Void viewSalesReport();
//    Void viewUserActivityReport();
//    Void financialReport();

}
