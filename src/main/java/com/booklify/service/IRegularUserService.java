package com.booklify.service;

import com.booklify.domain.RegularUser;

import java.util.List;
import java.util.Optional;

public interface IRegularUserService extends IService<RegularUser, Long>{

    RegularUser login(String email, String password);

    Optional<RegularUser> findByEmail(String email);
    List<RegularUser> findByFullName(String fullName);
    List<RegularUser> findAll();

}
