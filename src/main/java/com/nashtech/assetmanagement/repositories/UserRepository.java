package com.nashtech.assetmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nashtech.assetmanagement.entities.Account;

public interface UserRepository extends JpaRepository<Account, String>{

}
