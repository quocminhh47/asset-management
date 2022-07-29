package com.nashtech.assetmanagement.service;

import com.nashtech.assetmanagement.dto.request.RequestCreateAsset;
import com.nashtech.assetmanagement.dto.response.ResponseAssetDTO;

public interface AssetService {
    ResponseAssetDTO createAsset(RequestCreateAsset requestCreateAsset);
}
