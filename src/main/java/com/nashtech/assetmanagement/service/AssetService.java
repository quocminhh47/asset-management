package com.nashtech.assetmanagement.service;

import java.util.List;

import com.nashtech.assetmanagement.dto.request.EditAssetRequestDto;
import com.nashtech.assetmanagement.dto.request.CreateAssetRequestDto;
import com.nashtech.assetmanagement.dto.response.AssetResponseDto;
import com.nashtech.assetmanagement.dto.response.EditAssetResponseDto;
import com.nashtech.assetmanagement.dto.response.ListAssetResponseDto;
import com.nashtech.assetmanagement.dto.response.ResponseAssetDto;
import com.nashtech.assetmanagement.dto.response.MessageResponse;

public interface AssetService {
	ResponseAssetDto createAsset(CreateAssetRequestDto requestCreateAsset);
	
	ListAssetResponseDto getListAsset(String userId, List<String> categoryId, List<String> state, String keyword,
			String sortBy, String sortDirection, Integer page, Integer size);

	List<AssetResponseDto> getAssetByCodeOrNameAndLocationCode(String text, String locationCode);

	EditAssetResponseDto editAsset(EditAssetRequestDto editAssetRequest, String assetCode);

	//582 - Delete asset
	MessageResponse deleteAssetByAssetCode(String assetCode);
}
