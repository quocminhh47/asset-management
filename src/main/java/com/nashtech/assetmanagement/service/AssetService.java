package com.nashtech.assetmanagement.service;

import com.nashtech.assetmanagement.dto.request.EditAssetRequest;
import com.nashtech.assetmanagement.dto.request.RequestCreateAsset;
import com.nashtech.assetmanagement.dto.response.*;

import java.util.List;

public interface AssetService {
	ResponseAssetDTO createAsset(RequestCreateAsset requestCreateAsset);
	
	ListAssetResponseDto getListAsset(String userId, List<String> categoryId, List<String> state, String keyword,
			String sortBy, String sortDirection, Integer page, Integer size);

	List<ResponseAssetAndCategory> getAssetByCodeOrNameAndLocationCode(String text, String locationCode);

	EditAssetResponse editAsset(EditAssetRequest editAssetRequest, String assetCode);

	//582 - Delete asset
	ResponseMessage deleteAssetByAssetCode(String assetCode);
}
