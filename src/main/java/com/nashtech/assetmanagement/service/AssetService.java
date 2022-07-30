package com.nashtech.assetmanagement.service;

import java.util.List;

import com.nashtech.assetmanagement.dto.request.RequestCreateAsset;
import com.nashtech.assetmanagement.dto.response.ListAssetResponseDto;
import com.nashtech.assetmanagement.dto.response.ResponseAssetDTO;

public interface AssetService {
    ResponseAssetDTO createAsset(RequestCreateAsset requestCreateAsset);

	ListAssetResponseDto getListAsset(String userId, List<String> categoryId, List<String> state, String keyword, Integer page,
			Integer size);
}
