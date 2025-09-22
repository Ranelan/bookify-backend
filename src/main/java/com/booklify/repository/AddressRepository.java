package com.booklify.repository;

import com.booklify.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<Address> findByPostalCode(String postalCode);

    List<Address> findByCity(String city);

    List<Address> findByCountryAndProvince(String country, String province);

    Optional<Address> findByStreetAndSuburb(String street, String suburb);

    Optional<Address> findByUser_Id(Long userId);

}
