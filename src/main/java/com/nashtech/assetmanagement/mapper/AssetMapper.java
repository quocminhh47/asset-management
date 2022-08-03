package com.nashtech.assetmanagement.mapper;


import com.nashtech.assetmanagement.dto.request.EditAssetRequest;
import com.nashtech.assetmanagement.dto.request.RequestCreateAsset;
import com.nashtech.assetmanagement.dto.response.AssetResponseDto;
import com.nashtech.assetmanagement.dto.response.EditAssetResponse;
import com.nashtech.assetmanagement.dto.response.ResponseAssetAndCategory;
import com.nashtech.assetmanagement.dto.response.ResponseAssetDTO;
import com.nashtech.assetmanagement.entities.Asset;
import com.nashtech.assetmanagement.exception.DateInvalidException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.sql.Date;
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

    public Asset mapEditAssetRequestToEntity(EditAssetRequest request, Asset asset) {
        try {
            Date installedDate = Date.valueOf(request.getInstalledDate());
            Date dateNow = new Date(new java.util.Date().getTime());

            if (installedDate.after(dateNow)) throw new DateInvalidException(
                    "Date.is.must.before.today:" + dateNow.toString().replaceAll(" ", "."));
            mapper.map(request, asset);
            asset.setInstalledDate(installedDate);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Date.format.is.not.valid", e);
        }

        return asset;
    }

    public EditAssetResponse mapToEditAssetResponse(Asset asset) {
        return mapper.map(asset, EditAssetResponse.class);
    }
}
