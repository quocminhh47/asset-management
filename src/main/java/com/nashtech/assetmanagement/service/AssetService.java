package com.nashtech.assetmanagement.service;

import com.nashtech.assetmanagement.dto.request.RequestCreateAsset;
import com.nashtech.assetmanagement.dto.response.ResponseAssetDTO;

import java.util.List;

public interface AssetService {
    ResponseAssetDTO createAsset(RequestCreateAsset requestCreateAsset);

    List<ResponseAssetDTO> getAssetByCodeOrNameAndLocationCode(String text, String locationCode);
}
