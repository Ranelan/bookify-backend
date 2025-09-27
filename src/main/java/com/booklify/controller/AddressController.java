package com.booklify.controller;

import com.booklify.domain.Address;
import com.booklify.dto.AddressDto;
import com.booklify.service.IAddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final IAddressService addressService;

    public AddressController(IAddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<AddressDto> createAddress(@RequestBody AddressDto dto) {
        Address saved = addressService.save(AddressDto.toEntity(dto));
        return ResponseEntity.ok(AddressDto.fromEntity(saved));
    }

    @GetMapping("/{postalCode}")
    public ResponseEntity<AddressDto> getAddressByPostalCode(@PathVariable String postalCode) {
        Address found = addressService.findById(postalCode);
        return (found != null)
                ? ResponseEntity.ok(AddressDto.fromEntity(found))
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/{postalCode}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable String postalCode,
                                                    @RequestBody AddressDto dto) {
        Address existing = addressService.findById(postalCode);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        // Preserve ID and postalCode from existing entity
        dto.setId(existing.getId());
        dto.setPostalCode(postalCode);

        Address updated = addressService.update(AddressDto.toEntity(dto));
        return ResponseEntity.ok(AddressDto.fromEntity(updated));
    }

    @DeleteMapping("/{postalCode}")
    public ResponseEntity<Void> deleteAddress(@PathVariable String postalCode) {
        boolean deleted = addressService.deleteById(postalCode);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<AddressDto>> getAllAddresses() {
        List<Address> all = addressService.getAll();
        List<AddressDto> dtos = all.stream()
                .map(AddressDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
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