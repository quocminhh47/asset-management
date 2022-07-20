package com.nashtech.assetmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nashtech.assetmanagement.entities.Assignment;

public interface AssignmentRepository extends JpaRepository<Assignment, String>{

}
