package com.booklify.repository;

import com.booklify.domain.RegularUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegularUserRepository extends JpaRepository<RegularUser, Long> {

    Optional<RegularUser> findByEmail(String email);
    List<RegularUser> findByFullName(String fullName);

}