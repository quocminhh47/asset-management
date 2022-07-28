package com.nashtech.assetmanagement.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nashtech.assetmanagement.entities.Asset;
import com.nashtech.assetmanagement.entities.Category;
import com.nashtech.assetmanagement.entities.Users;
import com.nashtech.assetmanagement.enums.AssetState;

public interface AssetRepository extends JpaRepository<Asset, String> {

	Page<Asset> findByStateInAndUserAndCategoryAndAssetCodeContainingIgnoreCaseOrAssetNameContainingIgnoreCase(
			List<AssetState> assetState, Users user, Category category, String assetcode, String assetname,
			Pageable pageable);

	Long countByStateInAndUserAndCategoryAndAssetCodeContainingIgnoreCaseOrAssetNameContainingIgnoreCase(
			List<AssetState> assetState, Users user, Category category, String assetcode, String assetname);

//=======================

	Page<Asset> findByStateInAndUserAndAssetCodeContainingIgnoreCaseOrAssetNameContainingIgnoreCase(
			List<AssetState> assetState, Users user, String assetcode, String assetname, Pageable pageable);

	Long countByStateInAndUserAndAssetCodeContainingIgnoreCaseOrAssetNameContainingIgnoreCase(
			List<AssetState> assetState, Users user, String assetcode, String assetname);

	// =======================
	Page<Asset> findByUserAndAssetCodeContainingIgnoreCaseOrAssetNameContainingIgnoreCase(Users user, String assetcode,
			String assetname, Pageable pageable);

	Long countByUserAndAssetCodeContainingIgnoreCaseOrAssetNameContainingIgnoreCase(Users user, String assetcode,
			String assetname);
	// =======================

	Page<Asset> findByUserAndCategoryAndAssetCodeContainingIgnoreCaseOrAssetNameContainingIgnoreCase(Users user,
			Category category, String assetcode, String assetname, Pageable pageable);

	Long countByUserAndCategoryAndAssetCodeContainingIgnoreCaseOrAssetNameContainingIgnoreCase(Users user,
			Category category, String assetcode, String assetname);
	
	// =======================
}
