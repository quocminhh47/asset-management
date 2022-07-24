package com.nashtech.assetmanagement.repositories;

import com.nashtech.assetmanagement.entities.Role;
import com.nashtech.assetmanagement.entities.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, String> {
    Optional<Users> findByUserName(String userName);

    boolean existsByUserName(String userName);

    @Query(value = " select u from Users u where not u.staffCode = :staffCode and u.location.code = :location")
    Page<Users> findAllByOrderByFirstNameAsc(Pageable pageable, @Param("staffCode") String staffCode, @Param("location") String location);


    @Query(value = "SELECT * FROM users u" +
            " where (( LOWER(u.staff_code) like %:text%) or" +
            " (concat(u.first_name, u.last_name)) like %:text%)" +
            "and u.location_id = :location and u.staff_code != :loggedStaffCode",
            nativeQuery = true)
    Page<Users> searchByStaffCodeOrName(@Param("text") String text,
                                        @Param("loggedStaffCode") String loggedStaffCode,
                                        @Param(("location")) String adminLocation,
                                        Pageable pageable);

    @Query(value = "select u from Users u " +
            "where u.role = :role and " +
            "not u.staffCode = :staffCode " +
            "and u.location.code = :location")
    Page<Users> findUsersByRole(Pageable pageable,
                                @Param("role") Role role,
                                @Param("staffCode") String loggedStaffCode,
                                @Param("location") String location);

    @Query(value = "select count(*) FROM users WHERE staff_code like 'SD%'",nativeQuery = true)
    int countUsersByStaffCode();
    @Query(value = "select count(*) from users where first_name= :firstname AND last_name=:lastname",nativeQuery = true)
    int countUsersByFirstNameAndLastName(@Param("firstname")String firstName, @Param("lastname")String lastName);

    Optional<Users> findByStaffCode(String staffCode);
}
