package com.booklify.repository;

import com.booklify.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdminRepository extends JpaRepository <Admin, Long>{

    Optional<Admin> findByEmail(String email);

    List<Admin> findByFullName(String fullName);
}