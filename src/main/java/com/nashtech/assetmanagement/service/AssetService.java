package com.nashtech.assetmanagement.service;

import com.nashtech.assetmanagement.dto.request.CreateAssetRequestDto;
import com.nashtech.assetmanagement.dto.request.EditAssetRequestDto;
import com.nashtech.assetmanagement.dto.request.GetAssetListRequestDto;
import com.nashtech.assetmanagement.dto.response.*;

import java.util.List;

public interface AssetService {
    ResponseAssetDto createAsset(CreateAssetRequestDto requestCreateAsset);

    ListAssetResponseDto getListAsset(GetAssetListRequestDto requestDto);

    ListSearchingAssetResponseDto getAssetByCodeOrNameAndLocationCode(String text);

    EditAssetResponseDto editAsset(EditAssetRequestDto editAssetRequest, String assetCode);

    MessageResponse deleteAssetByAssetCode(String assetCode);

    AssetReportResponseDto getAssetReportList(int pageNo, int pageSize);

    List<IAssetReportResponseDto> getAllAssetReport();
}
