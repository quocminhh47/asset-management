package com.nashtech.assetmanagement.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nashtech.assetmanagement.dto.request.RequestCreateAsset;
import com.nashtech.assetmanagement.dto.response.AssetResponseDto;
import com.nashtech.assetmanagement.dto.response.ListAssetResponseDto;
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
		assetServiceImpl = new AssetServiceImpl(assetRepository, categoryRepository, userRepository, locationRepository,
				assetMapper);
	}

	@Test
	public void createAsset_WhenRequestValid_Expect_ReturnAsset() {
		RequestCreateAsset requestCreateAsset = new RequestCreateAsset("Lap top", "LT", "good", AssetState.AVAILABLE,
				null, "HN", "SD0001");
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
		ArgumentCaptor<String> assetCodeCapture = ArgumentCaptor.forClass(java.lang.String.class);
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

	// ===========US 579=======
	@DisplayName("Test for get list asset by user but user_id not found")
	@Test
	void getListAsset_ShouldThrownExceptionUserNotFound_WhenUserIdNotExist() {
		List<String> listStates = mock(List.class);
		List<String> listcategories = mock(List.class);
		when(userRepository.findById("SD001")).thenReturn(Optional.empty());
		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			assetServiceImpl.getListAsset("SD001", listcategories, listStates, "keyword", "sortBy", "sortDirection", 1, 1);
		});
		assertThat(exception.getMessage()).isEqualTo("user.not.found.with.code:SD001");
	}

	@DisplayName("Test for get list asset by user and states, and categories or asset name or asset code")
	@Test
	void getListAsset_ShouldReturnListAssetResponseDto_WhenUserIdExistAndCategoriesAndStates() {
		Users entity = mock(Users.class);
		Page<Asset> pageAsset = mock(Page.class);
		List<Asset> listAsset = mock(List.class);
		List<AssetResponseDto> expectList = mock(List.class);
		
		List<String> listStates = new ArrayList<String>();
		listStates.add("AVAILABLE");
		listStates.add("NOT_AVAILABLE");
		
		List<String> listcategories = new ArrayList<String>();
		listcategories.add("Laptop");
		listcategories.add("PC");

		when(userRepository.findById("SD001")).thenReturn(Optional.of(entity));
		when(assetRepository.getListAsset(eq("SD001"), Mockito.any(List.class), Mockito.any(List.class), eq("a"),
				Mockito.any(Pageable.class))).thenReturn(pageAsset);
		when(pageAsset.getTotalPages()).thenReturn(2);
		when(pageAsset.getContent()).thenReturn(listAsset);
		when(assetMapper.mapperListAsset(listAsset)).thenReturn(expectList);
		
		ListAssetResponseDto actual = assetServiceImpl.getListAsset("SD001", listcategories, listStates ,"a", "code", "DESC", 1, 2);

		ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
		ArgumentCaptor<List> captorlistCategories = ArgumentCaptor.forClass(List.class);
		ArgumentCaptor<List> captorlistStates = ArgumentCaptor.forClass(List.class);
		
		verify(assetRepository).getListAsset(eq("SD001"), captorlistCategories.capture(), captorlistStates.capture(), eq("a"), captor.capture());
		Pageable pageable = captor.getValue();
		
		assertThat(pageable.getPageNumber()).isEqualTo(0);
		assertThat(pageable.getPageSize()).isEqualTo(2);
		assertThat(actual.getList()).isEqualTo(expectList);
		assertThat(actual.getTotalPages()).isEqualTo(2);
	}
	
	@DisplayName("Test for get list asset by user and categories or asset name or asset code")
	@Test
	void getListAsset_ShouldReturnListAssetResponseDto_WhenUserIdExistAndCategories() {
		Users entity = mock(Users.class);
		Page<Asset> pageAsset = mock(Page.class);
		List<Asset> listAsset = mock(List.class);
		List<AssetResponseDto> expectList = mock(List.class);
		
		List<String> listStates = mock(List.class);
		List<String> listcategories = new ArrayList<>();
		listcategories.add("Laptop");
		listcategories.add("PC");

		when(userRepository.findById("SD001")).thenReturn(Optional.of(entity));
		when(listStates.size()).thenReturn(0);
		when(assetRepository.getListAssetByCategory(eq("SD001"), Mockito.any(List.class), eq("a"), Mockito.any(Pageable.class))).thenReturn(pageAsset);
		when(pageAsset.getTotalPages()).thenReturn(2);
		when(pageAsset.getContent()).thenReturn(listAsset);
		when(assetMapper.mapperListAsset(listAsset)).thenReturn(expectList);
		
		ListAssetResponseDto actual = assetServiceImpl.getListAsset("SD001", listcategories, listStates ,"a", "code", "DESC", 1, 2);

		ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
		ArgumentCaptor<List> captorlist = ArgumentCaptor.forClass(List.class);
		
		verify(assetRepository).getListAssetByCategory(eq("SD001"), captorlist.capture(), eq("a"), captor.capture());
		Pageable pageable = captor.getValue();
		
		assertThat(pageable.getPageNumber()).isEqualTo(0);
		assertThat(pageable.getPageSize()).isEqualTo(2);
		assertThat(actual.getList()).isEqualTo(expectList);
		assertThat(actual.getTotalPages()).isEqualTo(2);
	}
	
	@DisplayName("Test for get list asset by user and states or asset name or asset code")
	@Test
	void getListAsset_ShouldReturnListAssetResponseDto_WhenUserIdExistAndState() {
		Users entity = mock(Users.class);
		Page<Asset> pageAsset = mock(Page.class);
		List<Asset> listAsset = mock(List.class);
		List<AssetResponseDto> expectList = mock(List.class);
		
		List<String> listStates = new ArrayList<>();
		listStates.add("AVAILABLE");
		listStates.add("NOT_AVAILABLE");
		
		List<String> listcategories = mock(List.class);
		
		when(userRepository.findById("SD001")).thenReturn(Optional.of(entity));
		when(listcategories.size()).thenReturn(0);
		when(assetRepository.getListAssetByState(eq("SD001"), Mockito.any(List.class), eq("a"), Mockito.any(Pageable.class))).thenReturn(pageAsset);
		when(pageAsset.getTotalPages()).thenReturn(2);
		when(pageAsset.getContent()).thenReturn(listAsset);
		when(assetMapper.mapperListAsset(listAsset)).thenReturn(expectList);
		
		ListAssetResponseDto actual = assetServiceImpl.getListAsset("SD001", listcategories, listStates ,"a", "code", "DESC", 1, 2);
		ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
		ArgumentCaptor<List> captorlist = ArgumentCaptor.forClass(List.class);
		
		verify(assetRepository).getListAssetByState(eq("SD001"), captorlist.capture(), eq("a"), captor.capture());
		Pageable pageable = captor.getValue();
		
//		List<String> listStatesExpect =  captorlist.getValue();
		
		assertThat(pageable.getPageNumber()).isEqualTo(0);
		assertThat(pageable.getPageSize()).isEqualTo(2);
//		assertThat(listStatesExpect).isEqualTo(listStates);
		assertThat(actual.getList()).isEqualTo(expectList);
		assertThat(actual.getTotalPages()).isEqualTo(2);
	}
}
