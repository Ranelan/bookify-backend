package com.booklify.service.impl;

import com.booklify.domain.RegularUser;
import com.booklify.repository.RegularUserRepository;
import com.booklify.service.IRegularUserService;
import com.booklify.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RegularUserService implements IRegularUserService {

    private final RegularUserRepository regularUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public RegularUserService(RegularUserRepository regularUserRepository,
                              PasswordEncoder passwordEncoder,
                              JwtUtil jwtUtil) {
        this.regularUserRepository = regularUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public RegularUser login(String email, String password) {
        Optional<RegularUser> optionalUser = regularUserRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            RegularUser user = optionalUser.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                RegularUser updated = new RegularUser.RegularUserBuilder()
                        .copy(user)
                        .setLastLogin(LocalDateTime.now())
                        .build();
                regularUserRepository.save(updated);
                return updated;
            }
        }
        throw new RuntimeException("Invalid email or password");
    }

    @Override
    public Optional<RegularUser> findByEmail(String email) {
        return regularUserRepository.findByEmail(email);
    }

    @Override
    public List<RegularUser> findByFullName(String fullName) {
        return regularUserRepository.findByFullName(fullName);
    }

    @Override
    public List<RegularUser> findAll() {
        return regularUserRepository.findAll();
    }

    @Override
    public RegularUser save(RegularUser entity) {
        Optional<RegularUser> existing = regularUserRepository.findByEmail(entity.getEmail());

        if (existing.isPresent() && (entity.getId() == null || !existing.get().getId().equals(entity.getId()))) {
            throw new IllegalArgumentException("Email already exists.");
        }

        if (entity.getPassword() == null || entity.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        String encodedPassword = passwordEncoder.encode(entity.getPassword());

        RegularUser userToSave = new RegularUser.RegularUserBuilder()
                .copy(entity)
                .setPassword(encodedPassword)
                .build();

        return regularUserRepository.save(userToSave);
    }


    @Override
    public RegularUser findById(Long id) {
        return regularUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public RegularUser update(RegularUser entity) {
        if (entity.getId() != null && regularUserRepository.existsById(entity.getId())) {
            RegularUser existing = regularUserRepository.findById(entity.getId()).orElse(null);
            if (existing != null) {
                String encodedPassword = entity.getPassword() != null && !entity.getPassword().isBlank()
                        ? passwordEncoder.encode(entity.getPassword())
                        : existing.getPassword();

                RegularUser updated = new RegularUser.RegularUserBuilder()
                        .copy(existing)
                        .setFullName(entity.getFullName())
                        .setEmail(entity.getEmail())
                        .setPassword(encodedPassword)
                        .setBio(entity.getBio())
                        .setSellerRating(entity.getSellerRating())
                        .build();

                return regularUserRepository.save(updated);
            }
        }
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        if (regularUserRepository.existsById(id)) {
            regularUserRepository.deleteById(id);
        }
        return false;
    }
}
