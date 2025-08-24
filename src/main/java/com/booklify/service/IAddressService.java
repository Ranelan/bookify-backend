package com.booklify.service;

import com.booklify.domain.Address;

import java.util.List;

public interface IAddressService extends IService<Address,String> {
    List<Address>getAll();
    void deleteAll();
}
