package com.nashtech.assetmanagement.mapper;


import com.nashtech.assetmanagement.dto.request.RequestCreateAsset;
import com.nashtech.assetmanagement.dto.response.AssetResponseDto;
import com.nashtech.assetmanagement.dto.response.ResponseAssetAndCategory;
import com.nashtech.assetmanagement.dto.response.ResponseAssetDTO;
import com.nashtech.assetmanagement.entities.Asset;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AssetMapper {
    private final ModelMapper mapper;

    public AssetMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public ResponseAssetDTO assetToResponseAssetDTO(Asset asset) {
        return mapper.map(asset, ResponseAssetDTO.class);
    }

    public Asset RequestAssetToAsset(RequestCreateAsset requestCreateAsset) {
        return mapper.map(requestCreateAsset, Asset.class);
    }

    public List<AssetResponseDto> mapperListAsset(List<Asset> list) {
        List<AssetResponseDto> result = list.stream().map(item -> mapper.map(item, AssetResponseDto.class))
                .collect(Collectors.toList());
        return result;
    }

    public List<ResponseAssetAndCategory> getAssetListToResponseAssetDTOList(List<Asset> assetList) {
        List<ResponseAssetAndCategory> responseList = assetList.stream()
                .map(asset -> mapper.map(asset, ResponseAssetAndCategory.class)).collect(Collectors.toList());
        return responseList;
    }
}
