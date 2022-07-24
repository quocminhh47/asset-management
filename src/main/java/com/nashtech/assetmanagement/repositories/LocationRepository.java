package com.nashtech.assetmanagement.repositories;

import com.nashtech.assetmanagement.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, String>{

    boolean existsByName(String locationName);

    Location findByName(String locationName);

}
