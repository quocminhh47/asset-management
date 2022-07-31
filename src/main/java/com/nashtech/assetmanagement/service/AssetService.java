package com.nashtech.assetmanagement.service;

import com.nashtech.assetmanagement.dto.request.RequestCreateAsset;
import com.nashtech.assetmanagement.dto.response.ListAssetResponseDto;
import com.nashtech.assetmanagement.dto.response.ResponseAssetAndCategory;
import com.nashtech.assetmanagement.dto.response.ResponseAssetDTO;

import java.util.List;

public interface AssetService {
    ResponseAssetDTO createAsset(RequestCreateAsset requestCreateAsset);

    ListAssetResponseDto getListAsset(String userId, List<String> categoryId, List<String> state, String keyword, Integer page,
                                      Integer size);

    List<ResponseAssetAndCategory> getAssetByCodeOrNameAndLocationCode(String text, String locationCode);
}
