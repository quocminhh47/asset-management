package com.nashtech.assetmanagement.service;

import com.nashtech.assetmanagement.dto.request.CreateRequestReturningAssetRequestDto;
import com.nashtech.assetmanagement.dto.request.RequestReturningRequestGetListDto;
import com.nashtech.assetmanagement.dto.request.ReturningRequestDto;
import com.nashtech.assetmanagement.dto.response.CreateRequestReturningResponseDto;
import com.nashtech.assetmanagement.dto.response.ListRequestReturningResponseDto;
import com.nashtech.assetmanagement.dto.response.ListStateRequestReturningResponseDto;
import com.nashtech.assetmanagement.dto.response.MessageResponse;
import com.nashtech.assetmanagement.dto.response.RequestReturningResponseDto;

public interface RequestReturningService {

 	ListRequestReturningResponseDto getListRequestReturning(RequestReturningRequestGetListDto dto);

 	ListStateRequestReturningResponseDto getRequestReturningState();

	CreateRequestReturningResponseDto createRequestReturningAsset(CreateRequestReturningAssetRequestDto createRequestReturningAssetRequestDto);

	RequestReturningResponseDto completeReturningRequest(ReturningRequestDto requestDto);

    MessageResponse cancelRequestReturningAssignment(Long id);
}
