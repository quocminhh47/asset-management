package com.nashtech.assetmanagement.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.nashtech.assetmanagement.entities.Asset;
import com.nashtech.assetmanagement.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nashtech.assetmanagement.entities.Asset;
import com.nashtech.assetmanagement.entities.Users;
import com.nashtech.assetmanagement.enums.AssetState;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, String>{
    boolean existsAssetByCode(String assetCode);

	@Query("select e from Asset e where  e.user.staffCode = :user "
			+ "and ( lower(e.code) like lower(concat('%', :search, '%')) or (lower(e.name) like lower(concat('%', :search, '%'))))"
			+ "and (e.state in :state) and (e.category.id in :category)")
	Page<Asset> getListAsset(@Param("user") String user, @Param("category") List<String> category, @Param("state") List<AssetState> state ,
			@Param("search") String search, Pageable pageable);

	@Query("select e from Asset e where  e.user.staffCode = :user "
			+ "and ( lower(e.code) like lower(concat('%', :search, '%')) or (lower(e.name) like lower(concat('%', :search, '%'))))"
			+ "and (e.category.id in :category)")
	Page<Asset> getListAssetByCategory(@Param("user") String user, @Param("category") List<String> category,
			@Param("search") String search, Pageable pageable);

	@Query("select e from Asset e where  e.user.staffCode = :user "
			+ "and ( lower(e.code) like lower(concat('%', :search, '%')) or (lower(e.name) like lower(concat('%', :search, '%'))))"
			+ "and (e.state in :state)")
	Page<Asset> getListAssetByState(@Param("user") String user, @Param("state") List<AssetState> state ,
			@Param("search") String search, Pageable pageable);

	Page<Asset> findByUser(Users users, Pageable pageable);

    @Query(value = "select * from asset where " +
            "(lower(asset_name) LIKE %:text% or lower(asset_code) like %:text%)" +
            " and asset.location_id =:locationCode" +
            " and state='AVAILABLE'",nativeQuery = true)
    List<Asset> findAssetByNameOrCodeAndLocationCode(String text,String locationCode);
}
