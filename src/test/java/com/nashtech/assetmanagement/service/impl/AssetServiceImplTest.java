package com.nashtech.assetmanagement.service.impl;

import com.nashtech.assetmanagement.dto.request.RequestCreateAsset;
import com.nashtech.assetmanagement.entities.Asset;
import com.nashtech.assetmanagement.entities.Category;
import com.nashtech.assetmanagement.mapper.AssetMapper;
import com.nashtech.assetmanagement.repositories.AssetRepository;
import com.nashtech.assetmanagement.repositories.CategoryRepository;
import com.nashtech.assetmanagement.repositories.LocationRepository;
import com.nashtech.assetmanagement.utils.GenerateRandomNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AssetServiceImplTest {

    private AssetRepository assetRepository;

    private CategoryRepository categoryRepository;

    private LocationRepository locationRepository;
    private AssetMapper assetMapper;
    private Asset asset;

    private AssetServiceImpl assetServiceImpl;

    @BeforeEach
    void setUp(){
        asset=mock(Asset.class);
        assetRepository=mock(AssetRepository.class);
        assetMapper=mock(AssetMapper.class);
        locationRepository=mock(LocationRepository.class);
        categoryRepository=mock(CategoryRepository.class);
        assetServiceImpl=new AssetServiceImpl(assetRepository
                ,categoryRepository,
                locationRepository,assetMapper);
    }
    @Test
    public void createAsset_WhenRequestValid_Expect_ReturnAsset(){
        RequestCreateAsset requestCreateAsset=mock(RequestCreateAsset.class);
        when(assetMapper.RequestAssetToAsset(requestCreateAsset)).thenReturn(asset);
        Category category=mock(Category.class);
        Optional<Category> categoryOptional=Optional.of(category);
        when(categoryRepository.findById("LT")).thenReturn(categoryOptional);

    }
}
