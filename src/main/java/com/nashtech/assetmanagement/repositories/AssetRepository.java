package com.nashtech.assetmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nashtech.assetmanagement.entities.Asset;

public interface AssetRepository extends JpaRepository<Asset, String>{

}
