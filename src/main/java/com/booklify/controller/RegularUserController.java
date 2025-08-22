package com.booklify.controller;


import com.booklify.domain.RegularUser;
import com.booklify.dto.RegularUserDto;
import com.booklify.service.impl.RegularUserService;
import com.booklify.util.JwtUtil;
import com.booklify.util.RegularUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/regular-user")
public class RegularUserController {

    @Autowired
    private RegularUserService regularUserService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/create")
    public ResponseEntity<RegularUserDto> createRegularUser(@RequestBody RegularUserDto dto) {
        // Add validation
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        RegularUser savedUser = regularUserService.save(RegularUserMapper.toEntity(dto));
        return ResponseEntity.ok(RegularUserMapper.toDto(savedUser));
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        RegularUser user = regularUserService.login(loginRequest.getEmail(), loginRequest.getPassword());
        String token = jwtUtil.generateToken(user.getEmail());

        RegularUserDto dto = new RegularUserDto(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getDateJoined(),
                user.getSellerRating(),
                user.getBio(),
                user.getLastLogin()
        );

        return ResponseEntity.ok(new LoginResponse(token, dto));
    }




    @GetMapping("getById/{id}")
    public ResponseEntity<RegularUserDto> getById(@PathVariable Long id) {
        RegularUser user = regularUserService.findById(id);
        return ResponseEntity.ok(RegularUserMapper.toDto(user));
    }

    @GetMapping("/getByEmail/{email}")
    public ResponseEntity<RegularUserDto> getByEmail(@PathVariable String email) {
        RegularUser user = regularUserService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return ResponseEntity.ok(RegularUserMapper.toDto(user));
    }

    @GetMapping("/getByFullName/{fullName}")
    public ResponseEntity<List<RegularUserDto>> getByFullName(@PathVariable String fullName) {
        List<RegularUserDto> dtos = regularUserService.findByFullName(fullName)
                .stream()
                .map(RegularUserMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<RegularUserDto>> getAllUsers() {
        List<RegularUserDto> dtos = regularUserService.findAll()
                .stream()
                .map(RegularUserMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<RegularUserDto> updateUser(@PathVariable Long id, @RequestBody RegularUserDto dto) {
        dto.setId(id);
        RegularUser updated = regularUserService.update(RegularUserMapper.toEntity(dto));
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(RegularUserMapper.toDto(updated));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        regularUserService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private static class LoginResponse {
        private final String token;
        private final RegularUserDto user;

        public LoginResponse(String token, RegularUserDto user) {
            this.token = token;
            this.user = user;
        }

        public String getToken() {
            return token;
        }

        public RegularUserDto getUser() {
            return user;
        }
    }

    public static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
