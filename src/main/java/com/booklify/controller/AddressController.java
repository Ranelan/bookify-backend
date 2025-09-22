package com.booklify.controller;

import com.booklify.domain.Address;
import com.booklify.service.IAddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final IAddressService addressService;

    public AddressController(IAddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<Address> createAddress(@RequestBody Address address) {
        Address saved = addressService.save(address);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{postalCode}")
    public ResponseEntity<Address> getAddressByPostalCode(@PathVariable String postalCode) {
        Address found = addressService.findById(postalCode);
        return found != null ? ResponseEntity.ok(found) : ResponseEntity.notFound().build();
    }

    @PutMapping
    public ResponseEntity<Address> updateAddress(@RequestBody Address address) {
        Address updated = addressService.update(address);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{postalCode}")
    public ResponseEntity<Void> deleteAddress(@PathVariable String postalCode) {
        boolean deleted = addressService.deleteById(postalCode);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Address>> getAllAddresses() {
        List<Address> all = addressService.getAll();
        return ResponseEntity.ok(all);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllAddresses() {
        addressService.deleteAll();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Address> getAddressByUserId(@PathVariable Long userId) {
        return addressService.findByUserId(userId)
                .map(address -> ResponseEntity.ok(address))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}