package com.nashtech.assetmanagement.mapper;


import com.nashtech.assetmanagement.dto.request.RequestCreateAsset;
import com.nashtech.assetmanagement.dto.response.ResponseAssetDTO;
import com.nashtech.assetmanagement.dto.response.ResponseCategoryDTO;
import com.nashtech.assetmanagement.entities.Asset;
import com.nashtech.assetmanagement.entities.Category;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;

import java.util.List;

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
}
