package com.nashtech.assetmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nashtech.assetmanagement.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, String>{

}
