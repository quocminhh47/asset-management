package com.nashtech.assetmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nashtech.assetmanagement.entities.Location;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, String>{

    boolean existsByName(String locationName);

    Location findByName(String locationName);

}
