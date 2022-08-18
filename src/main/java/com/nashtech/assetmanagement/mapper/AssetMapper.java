package com.nashtech.assetmanagement.mapper;

import com.nashtech.assetmanagement.dto.request.CreateAssetRequestDto;
import com.nashtech.assetmanagement.dto.request.EditAssetRequestDto;
import com.nashtech.assetmanagement.dto.response.*;
import com.nashtech.assetmanagement.entities.Asset;
import com.nashtech.assetmanagement.enums.AssetState;
import com.nashtech.assetmanagement.exception.DateInvalidException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
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

    public ResponseAssetDto assetToResponseAssetDTO(Asset asset) {
        return mapper.map(asset, ResponseAssetDto.class);
    }

    public Asset requestAssetToAsset(CreateAssetRequestDto requestCreateAsset) {
        return mapper.map(requestCreateAsset, Asset.class);
    }

    public List<AssetResponseDto> mapperListAsset(List<Asset> list) {
        return list.stream()
				.map(item -> mapper.map(item, AssetResponseDto.class))
                .collect(Collectors.toList());
    }

    public List<AssetState> mapperListStates(List<String> list) {
        return list.stream().map(item -> mapper.map(item, AssetState.class)).collect(Collectors.toList());
    }

    public ListSearchingAssetResponseDto getAssetListToResponseAssetDTOList(List<Asset> assetList) {
        List<AssetResponseDto> responseList = assetList.stream()
				.map(asset -> mapper.map(asset, AssetResponseDto.class))
                .collect(Collectors.toList());
        return new ListSearchingAssetResponseDto(responseList.size(), responseList);
    }

    public Asset mapEditAssetRequestToEntity(EditAssetRequestDto request, Asset asset) {
        try {
            Date installedDate = Date.valueOf(request.getInstalledDate());
            Date dateNow = new Date(new java.util.Date().getTime());

            if (installedDate.after(dateNow))
                throw new DateInvalidException("Date.is.must.before.today:" + dateNow.toString().replace(" ", "."));
            mapper.map(request, asset);
            asset.setInstalledDate(installedDate);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Date.format.is.not.valid", e);
        }

        return asset;
    }

    public EditAssetResponseDto mapToEditAssetResponse(Asset asset) {
        return mapper.map(asset, EditAssetResponseDto.class);
    }

    public AssetReportResponseDto mapToAssetReportDto(Page<IAssetReportResponseDto> iAssetPage) {
        List<IAssetReportResponseDto> assetReportContent = iAssetPage.getContent();
        return AssetReportResponseDto.builder()
                .assetContent(assetReportContent)
                .pageNo(iAssetPage.getNumber())
                .pageSize(iAssetPage.getSize())
                .totalElements(iAssetPage.getTotalElements())
                .totalPages(iAssetPage.getTotalPages())
                .last(iAssetPage.isLast())
                .build();
    }
}
