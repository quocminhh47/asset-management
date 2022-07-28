package com.nashtech.assetmanagement.service;

import java.util.List;

import com.nashtech.assetmanagement.dto.response.AssetResponseDto;
import com.nashtech.assetmanagement.dto.response.ListAssetResponseDto;

public interface AssetService {
	
	ListAssetResponseDto getListAsset(String userId, int pageNumber, int size, 
			String categoryId, String assetcode, String assetname, List<String> state);

	AssetResponseDto getOne(String assetId);

}
