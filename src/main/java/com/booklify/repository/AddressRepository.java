package com.booklify.repository;

import com.booklify.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address,String> {

    Optional<Address> findByPostalCode(String postalCode);

    Optional<Address> findById(Long id);





}
