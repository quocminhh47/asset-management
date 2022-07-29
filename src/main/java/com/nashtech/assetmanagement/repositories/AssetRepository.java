package com.nashtech.assetmanagement.repositories;

import com.nashtech.assetmanagement.entities.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, String>{
    boolean existsAssetByAssetCode(String assetCode);

    List<Asset> getAssetByAssetCodeContainingIgnoreCaseOrAssetNameContainingIgnoreCase(String assetCode,String  assetName);
}
