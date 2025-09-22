package com.booklify.service.impl;

import com.booklify.domain.Address;
import com.booklify.repository.AddressRepository;
import com.booklify.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService implements IAddressService {

    @Autowired
    private AddressRepository  addressRepository;


    public Address save(Address address) {
        // Check if address for this user already exists
        Optional<Address> existing = addressRepository.findByUser_Id(address.getUser().getId());
        if (existing.isPresent()) {
            return existing.get(); // Or throw a custom exception if you want
        }
        return addressRepository.save(address);
    }

    @Override
    public Address findById(String postalCode) {
        Optional<Address>optional=addressRepository.findByPostalCode(postalCode);
        return optional.orElse(null);
    }

    @Override
    public Address update(Address entity) {
        return addressRepository.save(entity);
    }

    @Override
    public boolean deleteById(String postalCode) {
        Optional<Address> existing = addressRepository.findByPostalCode(postalCode);
        if (existing.isPresent()) {
            addressRepository.delete(existing.get());
            return true;
        }
        return false;
    }



    @Override
    public List<Address> getAll() {
        return addressRepository.findAll();

    }

    @Override
    public void deleteAll() {
        addressRepository.deleteAll();

    }

    @Override
    public Optional<Address> findByUserId(Long userId) {
        return addressRepository.findByUser_Id(userId);
    }
}
