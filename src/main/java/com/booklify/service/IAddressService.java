package com.booklify.service;

import com.booklify.domain.Address;

import java.util.List;
import java.util.Optional;

public interface IAddressService extends IService<Address,String> {
    List<Address>getAll();
    void deleteAll();
    Optional<Address> findByUserId(Long userId);
}
