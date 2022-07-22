package com.nashtech.assetmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nashtech.assetmanagement.entities.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, String>{
    Optional<Users> findByUserName(String userName);
    boolean existsByUserName(String userName);

    @Query(value = "select count(*) FROM users WHERE staff_code like 'SD%'",nativeQuery = true)
    int countUsersByStaffCode();
    @Query(value = "select count(*) from users where first_name= :firstname AND last_name=:lastname",nativeQuery = true)
    int countUsersByFirstNameAndLastName(@Param("firstname")String firstName, @Param("lastname")String lastName);

    Optional<Users> findByStaffCode(String staffCode);
}
