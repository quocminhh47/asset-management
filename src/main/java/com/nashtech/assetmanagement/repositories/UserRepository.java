package com.nashtech.assetmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nashtech.assetmanagement.entities.Users;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, String>{
    Optional<Users> findByUserName(String userName);
    boolean existsByUserName(String userName);
}
