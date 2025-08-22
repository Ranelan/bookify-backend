package com.booklify.controller;

import com.booklify.domain.Order;

import com.booklify.service.impl.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        // Validate the order object
        if (order == null ) {
            return ResponseEntity.badRequest().build();
        }

        // Save the order using the service
        Order savedOrder = orderService.save(order);

        // Return the saved order with a 201 Created status
        return ResponseEntity.status(201).body(savedOrder);
    }

    @PutMapping("/update/{orderId}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long orderId, @RequestBody Order order) {
        // Validate the order object
        if (order == null || orderId == null) {
            return ResponseEntity.badRequest().build();
        }
        // Use builder to set the orderId since setOrderId does not exist
        Order updatedOrder = new Order.OrderBuilder()
                .copy(order)
                .setOrderId(orderId)
                .build();
        updatedOrder = orderService.update(updatedOrder);
        if (updatedOrder == null) {
            return ResponseEntity.notFound().build();
        }
        // Return the updated order with a 200 OK status
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        // Validate the orderId
        if (orderId == null) {
            return ResponseEntity.badRequest().build();
        }

        // Delete the order using the service
        boolean isDeleted = orderService.deleteById(orderId);

        if (!isDeleted) {
            return ResponseEntity.notFound().build();
        }

        // Return a 204 No Content status
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getById/{orderId}")
    public ResponseEntity<Order> findByOrderById(@PathVariable Long orderId) {
        // Validate the orderId
        if (orderId == null) {
            return ResponseEntity.badRequest().build();
        }

        // Find the order by ID using the service
        Order order = orderService.findById(orderId);

        if (order == null) {
            return ResponseEntity.notFound().build();
        }

        // Return the found order with a 200 OK status
        return ResponseEntity.ok(order);
    }

    @GetMapping("/getAll")
    public ResponseEntity<Iterable<Order>> getAll() {
        // Retrieve all orders using the service
        Iterable<Order> orders = orderService.getAll();

        // Return the list of orders with a 200 OK status
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/getByUserId/{userId}")
    public ResponseEntity<Iterable<Order>> getOrdersByUserId(@PathVariable Long userId) {
        // Validate the userId
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }

        // Retrieve orders by user ID using the service
        Iterable<Order> orders = orderService.findByRegularUserId(userId);

        if (orders == null) {
            return ResponseEntity.notFound().build();
        }

        // Return the list of orders with a 200 OK status
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/getByOrderDate/{orderDate}")
    public ResponseEntity<Iterable<Order>> getOrdersByOrderDate(@PathVariable String orderDate) {
        // Parse the orderDate string to LocalDateTime
        LocalDateTime parsedDate;
        try {
            parsedDate = LocalDateTime.parse(orderDate);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        // Retrieve orders by order date using the service
        Iterable<Order> orders = orderService.findByOrderDate(parsedDate);
        if (orders == null) {
            return ResponseEntity.notFound().build();
        }
        // Return the list of orders with a 200 OK status
        return ResponseEntity.ok(orders);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        if (ex.getMessage() != null && ex.getMessage().startsWith("Order not found")) {
            return ResponseEntity.status(404).body(ex.getMessage());
        }
        return ResponseEntity.status(500).body("Internal Server Error");
    }
}
