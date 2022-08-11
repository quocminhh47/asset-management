package com.nashtech.assetmanagement.service;

import com.nashtech.assetmanagement.dto.request.RequestReturningRequestGetListDto;

import com.nashtech.assetmanagement.dto.request.CreateRequestReturningAssetRequestDto;
import com.nashtech.assetmanagement.dto.response.CreateRequestReturningResponseDto;
import com.nashtech.assetmanagement.dto.response.ListRequestReturningResponseDto;

public interface RequestReturningService {

 	ListRequestReturningResponseDto getListRequestReturning(RequestReturningRequestGetListDto dto);

	//587 - Create request for returning asset
	CreateRequestReturningResponseDto createRequestReturningAsset(CreateRequestReturningAssetRequestDto createRequestReturningAssetRequestDto);

}
