package com.nashtech.assetmanagement.service;

import java.util.List;

import com.nashtech.assetmanagement.dto.request.CreateRequestReturningAssetRequestDto;
import com.nashtech.assetmanagement.dto.response.CreateRequestReturningResponseDto;
import com.nashtech.assetmanagement.dto.response.ListRequestReturningResponseDto;

public interface RequestReturningService {

	ListRequestReturningResponseDto getListRequestReturning(List<String> states, String returnedDate, String keyword,
			String sortBy, String sortDirection, Integer page, Integer size);

	//587 - Create request for returning asset
	CreateRequestReturningResponseDto createRequestReturningAsset(CreateRequestReturningAssetRequestDto createRequestReturningAssetRequestDto);
}
