package com.nashtech.assetmanagement.service;

import com.nashtech.assetmanagement.dto.request.CreateRequestReturningAssetRequestDto;
import com.nashtech.assetmanagement.dto.request.GetRequestReturningListRequestDto;
import com.nashtech.assetmanagement.dto.request.ReturningRequestDto;
import com.nashtech.assetmanagement.dto.response.*;

public interface RequestReturningService {

 	ListRequestReturningResponseDto getListRequestReturning(GetRequestReturningListRequestDto dto);

 	ListStateRequestReturningResponseDto getRequestReturningState();

	CreateRequestReturningResponseDto createRequestReturningAsset(CreateRequestReturningAssetRequestDto createRequestReturningAssetRequestDto);

	RequestReturningResponseDto completeReturningRequest(ReturningRequestDto requestDto);

    MessageResponse cancelRequestReturningAssignment(Long id);
}
