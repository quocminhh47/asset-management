package com.nashtech.assetmanagement.service.impl;

import com.nashtech.assetmanagement.dto.request.RequestCreateAsset;
import com.nashtech.assetmanagement.dto.response.ResponseAssetDTO;
import com.nashtech.assetmanagement.entities.Asset;
import com.nashtech.assetmanagement.entities.Category;
import com.nashtech.assetmanagement.entities.Location;
import com.nashtech.assetmanagement.entities.Users;
import com.nashtech.assetmanagement.exception.ResourceNotFoundException;
import com.nashtech.assetmanagement.mapper.AssetMapper;
import com.nashtech.assetmanagement.repositories.AssetRepository;
import com.nashtech.assetmanagement.repositories.CategoryRepository;
import com.nashtech.assetmanagement.repositories.LocationRepository;
import com.nashtech.assetmanagement.repositories.UserRepository;
import com.nashtech.assetmanagement.service.AssetService;
import com.nashtech.assetmanagement.utils.GenerateRandomNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;

    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;

    private final LocationRepository locationRepository;

    private final AssetMapper assetMapper;

    @Autowired
    public AssetServiceImpl(AssetRepository assetRepository, CategoryRepository categoryRepository, UserRepository userRepository, LocationRepository locationRepository, AssetMapper assetMapper) {
        this.assetRepository = assetRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
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
                                        "Can not find category has code: " + requestCreateAsset.getCategoryId()));
        asset.setCategory(category);
        Location location =
                locationRepository.findById(requestCreateAsset.getLocationId())
                        .orElseThrow(() -> new ResourceNotFoundException("Can not find " +
                                "category has code: " + requestCreateAsset.getLocationId()));
        asset.setLocation(location);
        Users users=
                userRepository.findById(requestCreateAsset.getUserId()).orElseThrow(()->new ResourceNotFoundException("Can not find " +
                        "user has id: " + requestCreateAsset.getUserId()));
        asset.setUser(users);
        asset = assetRepository.save(asset);
        return assetMapper.assetToResponseAssetDTO(asset);
    }

}
