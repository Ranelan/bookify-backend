package com.booklify.factory;

import com.booklify.domain.Address;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressFactoryTest {
//    private RegularUser user;
    //        user = new RegularUser.RegularUserBuilder()
//                .setFullName("Sesona Panca")
//                .setEmail("sesona@example.com")
//                .setPassword("securePassword123")
//                .build();

    @Test
    void createAddress_withValidData_shouldReturnAddress() {
        Address address = AddressFactory.createAddress(
                "Main Street",
                "Rosebank",
                "Cape Town",
                "Western Cape",
                "South Africa",
                "8001"
        );

        assertNotNull(address);
        assertEquals("Main Street", address.getStreet());
        assertEquals("Rosebank", address.getSuburb());
        assertEquals("Cape Town", address.getCity());
        assertEquals("Western Cape", address.getProvince());
        assertEquals("South Africa", address.getCountry());
        assertEquals("8001", address.getPostalCode());
        System.out.println(address);
    }

}