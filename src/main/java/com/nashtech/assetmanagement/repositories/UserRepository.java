package com.nashtech.assetmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nashtech.assetmanagement.entities.Account;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Account, String>{
    Optional<Account> findByUserName(String userName);
    boolean existsByUserName(String userName);
}
