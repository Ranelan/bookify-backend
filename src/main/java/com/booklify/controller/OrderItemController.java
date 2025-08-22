package com.booklify.controller;


import com.booklify.domain.OrderItem;
import com.booklify.domain.enums.OrderStatus;
import com.booklify.service.impl.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orderItems")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @PostMapping("/create")
    public ResponseEntity<OrderItem> createOrderItem(@RequestBody OrderItem orderItem) {
        // Validate the orderItem object
        if (orderItem == null || orderItem.getOrder() == null || orderItem.getBook() == null || orderItem.getOrderStatus() == null) {
            return ResponseEntity.badRequest().build();
        }

        // Save the orderItem using the service
        OrderItem savedOrderItem = orderItemService.save(orderItem);

        // Return the saved orderItem with a 201 Created status
        return ResponseEntity.status(201).body(savedOrderItem);

    }

    @PutMapping("/update/{orderItemId}")
    public ResponseEntity<OrderItem> updateOrderItem(@PathVariable OrderItem orderItem) {
        // Validate the orderItem object
        if (orderItem == null || orderItem.getOrderItemId() == null) {
            return ResponseEntity.badRequest().build();
        }
        // Update the orderItem using the service
        OrderItem updatedOrderItem = orderItemService.update(orderItem);
        if (updatedOrderItem == null) {
            return ResponseEntity.notFound().build();
        }
        // Return the updated orderItem with a 200 OK status
        return ResponseEntity.ok(updatedOrderItem);
    }

    @DeleteMapping("/delete/{orderItemId}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long orderItemId) {
        // Validate the orderItemId
        if (orderItemId == null) {
            return ResponseEntity.badRequest().build();
        }

        // Delete the orderItem using the service
        boolean isDeleted = orderItemService.deleteById(orderItemId);

        if (!isDeleted) {
            return ResponseEntity.notFound().build();
        }

        // Return a 204 No Content status
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getByOrderItemId/{orderItemId}")
    public ResponseEntity<OrderItem> getOrderItemById(@PathVariable Long orderItemId) {
        // Validate the orderItemId
        if (orderItemId == null) {
            return ResponseEntity.badRequest().build();
        }
        // Find the orderItem by ID using the service
        OrderItem orderItem = orderItemService.findById(orderItemId);
        if (orderItem == null) {
            return ResponseEntity.notFound().build();
        }
        // Return the found orderItem with a 200 OK status
        return ResponseEntity.ok(orderItem);
    }

    @GetMapping("/getAll")
    public ResponseEntity<Iterable<OrderItem>> findAllOrderItems() {
        // Get all orderItems using the service
        Iterable<OrderItem> orderItems = orderItemService.findAll();

        // Return the list of orderItems with a 200 OK status
        return ResponseEntity.ok(orderItems);
    }

    @GetMapping("getByOrderStatus/{orderStatus}")
    public ResponseEntity<Iterable<OrderItem>> findByOrderStatus(@PathVariable String orderStatus) {
        // Validate the orderStatus
        if (orderStatus == null || orderStatus.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        // Find orderItems by orderStatus using the service
        Iterable<OrderItem> orderItems = orderItemService.findByOrderStatus(OrderStatus.valueOf(orderStatus));
        if (orderItems == null) {
            return ResponseEntity.notFound().build();
        }
        // Return the found orderItems with a 200 OK status
        return ResponseEntity.ok(orderItems);
    }

    @GetMapping("/getByOrderId/{orderId}")
    public ResponseEntity<Iterable<OrderItem>> findByOrderId(@PathVariable Long orderId) {
        // Validate the orderId
        if (orderId == null) {
            return ResponseEntity.badRequest().build();
        }
        // Find orderItems by orderId using the service
        Iterable<OrderItem> orderItems = orderItemService.findByOrderId(orderId);
        if (orderItems == null) {
            return ResponseEntity.notFound().build();
        }
        // Return the found orderItems with a 200 OK status
        return ResponseEntity.ok(orderItems);
    }

    @GetMapping("/getByBookId/{bookId}")
    public ResponseEntity<Iterable<OrderItem>> findByBookId(@PathVariable Long bookId) {
        // Validate the bookId
        if (bookId == null) {
            return ResponseEntity.badRequest().build();
        }
        // Find orderItems by bookId using the service
        Iterable<OrderItem> orderItems = orderItemService.findByBookId(bookId);
        if (orderItems == null) {
            return ResponseEntity.notFound().build();
        }
        // Return the found orderItems with a 200 OK status
        return ResponseEntity.ok(orderItems);
    }


}


