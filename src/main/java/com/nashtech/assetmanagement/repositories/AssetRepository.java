package com.nashtech.assetmanagement.repositories;

import com.nashtech.assetmanagement.entities.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<Asset, String>{

}
