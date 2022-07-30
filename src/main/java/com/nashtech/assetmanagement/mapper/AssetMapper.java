package com.nashtech.assetmanagement.mapper;


import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.nashtech.assetmanagement.dto.request.RequestCreateAsset;
import com.nashtech.assetmanagement.dto.response.AssetResponseDto;
import com.nashtech.assetmanagement.dto.response.ResponseAssetDTO;
import com.nashtech.assetmanagement.entities.Asset;
import com.nashtech.assetmanagement.entities.Category;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AssetMapper {
    private final ModelMapper mapper;

    public AssetMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public ResponseAssetDTO assetToResponseAssetDTO(Asset asset){
        return mapper.map(asset,ResponseAssetDTO.class);
    }
    public Asset RequestAssetToAsset(RequestCreateAsset requestCreateAsset){
        return mapper.map(requestCreateAsset,Asset.class);
    }

    public List<AssetResponseDto> mapperListAsset(List<Asset> list) {
		List<AssetResponseDto> result = list.stream().map(item -> mapper.map(item, AssetResponseDto.class))
				.collect(Collectors.toList());
		return result;
	}
    public List<ResponseAssetDTO> getAssetListToResponseAssetDTOList(List<Asset> assetList){
        List<ResponseAssetDTO> responseList = assetList.stream()
                .map(asset -> mapper.map(asset,ResponseAssetDTO.class)).collect(Collectors.toList());
        return responseList;
    }
}
