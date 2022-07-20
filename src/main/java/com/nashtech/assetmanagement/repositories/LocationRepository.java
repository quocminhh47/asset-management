package com.nashtech.assetmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nashtech.assetmanagement.entities.Location;

public interface LocationRepository extends JpaRepository<Location, String>{

}
