package com.nashtech.assetmanagement.repositories;

import com.nashtech.assetmanagement.entities.Asset;
import com.nashtech.assetmanagement.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, String>{
    boolean existsAssetByCode(String assetCode);

    @Query(value = "select * from asset where " +
            "(lower(asset_name) LIKE %:text% or lower(asset_code) like %:text%)" +
            " and asset.location_id =:locationCode" +
            " and state='AVAILABLE'",nativeQuery = true)
    List<Asset> findAssetByNameOrCodeAndLocationCode(String text,String locationCode);
}
