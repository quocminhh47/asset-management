package com.nashtech.assetmanagement.service.impl;

import com.nashtech.assetmanagement.dto.request.RequestCreateAsset;
import com.nashtech.assetmanagement.dto.response.ResponseAssetAndCategory;
import com.nashtech.assetmanagement.dto.response.ResponseAssetDTO;
import com.nashtech.assetmanagement.entities.Asset;
import com.nashtech.assetmanagement.entities.Category;
import com.nashtech.assetmanagement.entities.Location;
import com.nashtech.assetmanagement.entities.Users;
import com.nashtech.assetmanagement.enums.AssetState;
import com.nashtech.assetmanagement.exception.ResourceNotFoundException;
import com.nashtech.assetmanagement.mapper.AssetMapper;
import com.nashtech.assetmanagement.repositories.AssetRepository;
import com.nashtech.assetmanagement.repositories.CategoryRepository;
import com.nashtech.assetmanagement.repositories.LocationRepository;
import com.nashtech.assetmanagement.repositories.UserRepository;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class AssetServiceImplTest {

    private AssetRepository assetRepository;

    private UserRepository userRepository;

    private CategoryRepository categoryRepository;

    private LocationRepository locationRepository;
    private AssetMapper assetMapper;
    private Asset asset;

    private AssetServiceImpl assetServiceImpl;

    @BeforeEach
    void setUp() {
        asset = mock(Asset.class);
        assetRepository = mock(AssetRepository.class);
        assetMapper = mock(AssetMapper.class);
        locationRepository = mock(LocationRepository.class);
        categoryRepository = mock(CategoryRepository.class);
        userRepository = mock(UserRepository.class);
        assetServiceImpl = new AssetServiceImpl(assetRepository
                , categoryRepository,
                userRepository, locationRepository, assetMapper);
    }

    @Test
    public void createAsset_WhenRequestValid_Expect_ReturnAsset() {
        RequestCreateAsset requestCreateAsset = new RequestCreateAsset("Lap top",
                "LT", "good", AssetState.AVAILABLE, null, "HN", "SD0001");
        // RequestCreateAsset requestCreateAsset=mock(RequestCreateAsset.class);
        when(assetMapper.RequestAssetToAsset(requestCreateAsset)).thenReturn(asset);
        Category category = mock(Category.class);
        Optional<Category> categoryOptional = Optional.of(category);
        Location location = mock(Location.class);
        Optional<Location> locationOptional = Optional.of(location);
        Users users = mock(Users.class);
        Optional<Users> usersOptional = Optional.of(users);
        when(userRepository.findById("SD0001")).thenReturn(usersOptional);
        when(categoryRepository.findById("LT")).thenReturn(categoryOptional);
        when(locationRepository.findById("HN")).thenReturn(locationOptional);
        when(assetRepository.save(asset)).thenReturn(asset);
        ResponseAssetDTO expected = mock(ResponseAssetDTO.class);
        when(assetMapper.assetToResponseAssetDTO(asset)).thenReturn(expected);
        ResponseAssetDTO actual = assetServiceImpl.createAsset(requestCreateAsset);
        ArgumentCaptor<String> assetCodeCapture =
                ArgumentCaptor.forClass(java.lang.String.class);
        verify(asset).setCode(assetCodeCapture.capture());
        verify(asset).setLocation(location);
        verify(asset).setCategory(category);
        verify(asset).setUser(users);
        assertThat(actual).isEqualTo(expected);
    }

    //US584-CreateNewAssignment
    @Test
    void getAssetList_ShouldReturnResponseAssetDtoList_WhenAssetExist() {
        Location location = mock(Location.class);
        List<Asset> assetList = mock(ArrayList.class);
        List<ResponseAssetAndCategory> responseList = mock(ArrayList.class);
        when(locationRepository.findById("locationCode")).thenReturn(Optional.of(location));
        when(assetRepository.findAssetByNameOrCodeAndLocationCode("text", "locationCode")).thenReturn(assetList);
        when(assetMapper.getAssetListToResponseAssetDTOList(assetList)).thenReturn(responseList);
        List<ResponseAssetAndCategory> result = assetServiceImpl.getAssetByCodeOrNameAndLocationCode("text", "locationCode");
        assertThat(result).isEqualTo(responseList);
    }
    @Test
    void getAssetList_ShouldThrowResourceNotFoundEx_WhenLocationCodeIncorrect(){
        when(locationRepository.findById("HCM")).thenReturn(Optional.empty());
        ResourceNotFoundException e = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> assetServiceImpl.getAssetByCodeOrNameAndLocationCode("text","HCM"));
        AssertionsForClassTypes.assertThat(e.getMessage()).isEqualTo("Location code not found");
    }

}
