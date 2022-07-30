package com.nashtech.assetmanagement.service.impl;

import com.nashtech.assetmanagement.dto.request.RequestCreateAsset;
import com.nashtech.assetmanagement.dto.response.ResponseAssetDTO;
import com.nashtech.assetmanagement.entities.Asset;
import com.nashtech.assetmanagement.entities.Category;
import com.nashtech.assetmanagement.entities.Location;
import com.nashtech.assetmanagement.exception.ResourceNotFoundException;
import com.nashtech.assetmanagement.mapper.AssetMapper;
import com.nashtech.assetmanagement.repositories.AssetRepository;
import com.nashtech.assetmanagement.repositories.CategoryRepository;
import com.nashtech.assetmanagement.repositories.LocationRepository;
import com.nashtech.assetmanagement.service.AssetService;
import com.nashtech.assetmanagement.utils.GenerateRandomNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;

    private final CategoryRepository categoryRepository;

    private final LocationRepository locationRepository;

    private final AssetMapper assetMapper;

    @Autowired
    public AssetServiceImpl(AssetRepository assetRepository, CategoryRepository categoryRepository, LocationRepository locationRepository, AssetMapper assetMapper) {
        this.assetRepository = assetRepository;
        this.categoryRepository = categoryRepository;
        this.locationRepository = locationRepository;
        this.assetMapper = assetMapper;
    }

    public String generateAssetCode(String prefix) {
        String code;
        do {
            code = prefix + GenerateRandomNumber.randomNumber();
        } while (assetRepository.existsAssetByCode(code));
        return code;
    }

    @Override
    public ResponseAssetDTO createAsset(RequestCreateAsset requestCreateAsset) {
        Asset asset = assetMapper.RequestAssetToAsset(requestCreateAsset);
        asset.setCode(generateAssetCode(requestCreateAsset.getCategoryId()));
        Category category =
                categoryRepository.findById(requestCreateAsset.getCategoryId())
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Can not find category has code:" + requestCreateAsset.getCategoryId()));
        asset.setCategory(category);
        Location location =
                locationRepository.findById(requestCreateAsset.getLocationId())
                        .orElseThrow(() -> new ResourceNotFoundException("Can not find " +
                                "category has code:" + requestCreateAsset.getLocationId()));
        asset.setLocation(location);
        asset = assetRepository.save(asset);
        return assetMapper.assetToResponseAssetDTO(asset);
    }

    @Override
    public List<ResponseAssetDTO> getAssetByCodeOrNameAndLocationCode(String text, String locationCode) {
        Optional<Location> locationOptional = locationRepository.findById(locationCode);
        if(locationOptional.isEmpty()){
            throw new ResourceNotFoundException("Location code not found");
        }
        List<Asset> assetList = assetRepository.findAssetByNameOrCodeAndLocationCode(text.toLowerCase(),locationCode);
        List<ResponseAssetDTO> responseList = assetMapper.getAssetListToResponseAssetDTOList(assetList);
        return responseList;
    }

}
