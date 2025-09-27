package com.booklify.factory;

import com.booklify.domain.Address;
import com.booklify.domain.Order;
import com.booklify.domain.RegularUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AddressFactoryTest {

    private RegularUser user;
    private List<Order> orders;

    @BeforeEach
    void setUp() {
        user = new RegularUser.RegularUserBuilder()
                .setId(235444255L)
                .setFullName("Sesona Panca")
                .setEmail("sesona@example.com")
                .setPassword("securePassword123")
                .setBio("COOL")
                .setDateJoined(LocalDateTime.MIN)
                .setLastLogin(LocalDateTime.now())
                .setSellerRating(4.5)
                .build();

        Order order = new Order.OrderBuilder()
                .setOrderId(123654356L)
                .setOrderDate(LocalDateTime.of(2025, 8, 27, 2, 36))
                .build();

        orders = Collections.singletonList(order);
    }

    @Test
    void createAddress_successfully() {
        Address address = AddressFactory.createAddress(
                "Main Street",
                "Rosebank",
                "Cape Town",
                "Western Cape",
                "South Africa",
                "8001",
                user,
                orders
        );

        assertNotNull(address, "Address should not be null");
        assertEquals("Main Street", address.getStreet());
        assertEquals("Rosebank", address.getSuburb());
        assertEquals("Cape Town", address.getCity());
        assertEquals("Western Cape", address.getProvince());
        assertEquals("South Africa", address.getCountry());
        assertEquals("8001", address.getPostalCode());
        System.out.println(address);
    }


}