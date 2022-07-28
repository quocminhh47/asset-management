package com.nashtech.assetmanagement.repositories;

import com.nashtech.assetmanagement.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String>{

    public boolean existsCategoriesById(String id);

}
