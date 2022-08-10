package com.nashtech.assetmanagement.service.impl;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.nashtech.assetmanagement.dto.response.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.nashtech.assetmanagement.dto.request.EditAssetRequestDto;
import com.nashtech.assetmanagement.dto.request.CreateAssetRequestDto;
import com.nashtech.assetmanagement.entities.Asset;
import com.nashtech.assetmanagement.entities.Assignment;
import com.nashtech.assetmanagement.entities.Category;
import com.nashtech.assetmanagement.entities.Location;
import com.nashtech.assetmanagement.entities.Users;
import com.nashtech.assetmanagement.enums.AssetState;
import com.nashtech.assetmanagement.exception.ResourceNotFoundException;
import com.nashtech.assetmanagement.mapper.AssetMapper;
import com.nashtech.assetmanagement.repositories.AssetRepository;
import com.nashtech.assetmanagement.repositories.AssignmentRepository;
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

	private AssignmentRepository assignmentRepository;

	private AssetServiceImpl assetServiceImpl;

	private AuthenticationServiceImpl authenticationServiceImpl;
	EditAssetRequestDto request;
	AssetState newAssetState = AssetState.NOT_AVAILABLE;

	@BeforeEach
	void setUp() {
		asset = mock(Asset.class);
		authenticationServiceImpl=mock(AuthenticationServiceImpl.class);
		assetRepository = mock(AssetRepository.class);
		assetMapper = mock(AssetMapper.class);
		locationRepository = mock(LocationRepository.class);
		categoryRepository = mock(CategoryRepository.class);
		userRepository = mock(UserRepository.class);
		assignmentRepository = mock(AssignmentRepository.class);
		assetServiceImpl = new AssetServiceImpl(assetRepository, categoryRepository, userRepository, locationRepository,
				assetMapper, assignmentRepository,authenticationServiceImpl);

		request = new EditAssetRequestDto("Dell inspriration 5432", "CPU 7200U, RAM 16GB", "2022-01-01", newAssetState);
	}

    @Test
    public void createAsset_ShouldReturnAsset_WhenRequestValid() {
        CreateAssetRequestDto requestCreateAsset = new CreateAssetRequestDto("Lap top", "LT", "good", AssetState.AVAILABLE,
                null);
		Users users=mock(Users.class);
		when(authenticationServiceImpl.getUser()).thenReturn(users);
        when(assetMapper.requestAssetToAsset(requestCreateAsset)).thenReturn(asset);
        Category category = mock(Category.class);
        Optional<Category> categoryOptional = Optional.of(category);
        when(categoryRepository.findById("LT")).thenReturn(categoryOptional);
        when(assetRepository.save(asset)).thenReturn(asset);
        ResponseAssetDto expected = mock(ResponseAssetDto.class);
        when(assetMapper.assetToResponseAssetDTO(asset)).thenReturn(expected);
        ResponseAssetDto actual = assetServiceImpl.createAsset(requestCreateAsset);
        ArgumentCaptor<String> assetCodeCapture = ArgumentCaptor.forClass(java.lang.String.class);
        verify(asset).setCode(assetCodeCapture.capture());
        verify(asset).setCategory(category);
        assertThat(actual).isEqualTo(expected);
    }

	// US584-CreateNewAssignment
	@Test
	void getAssetList_ShouldReturnResponseAssetDtoList_WhenAssetExist() {
		Users users = mock(Users.class);
		Location location = mock(Location.class);
		List<Asset> assetList = mock(ArrayList.class);
		List<AssetResponseDto> responseList = mock(ArrayList.class);
		when(authenticationServiceImpl.getUser()).thenReturn(users);
		when(users.getLocation()).thenReturn(location);
		when(location.getCode()).thenReturn("locationCode");
		when(assetRepository.findAssetByNameOrCodeAndLocationCode("text", "locationCode")).thenReturn(assetList);
		when(assetMapper.getAssetListToResponseAssetDTOList(assetList)).thenReturn(responseList);
		List<AssetResponseDto> result = assetServiceImpl.getAssetByCodeOrNameAndLocationCode("text");
		assertThat(result).isEqualTo(responseList);
	}

	@DisplayName("Given valid asset request then edit asset - positive case")
	@Test
	void editAsset_ShouldReturnEditAssetResponseDto_whenTheRequestIsValid() {
		// given
		String assetCode = "LA100001";
		Asset existAsset = mock(Asset.class);
		Asset mappedAsset = mock(Asset.class);
		Asset savedAsset = mock(Asset.class);
		EditAssetResponseDto expectedResponse = mock(EditAssetResponseDto.class);

		when(assetRepository.findById(assetCode)).thenReturn(Optional.of(existAsset));
		when(existAsset.getState()).thenReturn(request.getState());
		when(assetMapper.mapEditAssetRequestToEntity(request, existAsset)).thenReturn(mappedAsset);
		when(assetRepository.save(mappedAsset)).thenReturn(savedAsset);
		when(assetMapper.mapToEditAssetResponse(savedAsset)).thenReturn(expectedResponse);

		// when
		EditAssetResponseDto actualResponse = assetServiceImpl.editAsset(request, assetCode);

		// then
		assertThat(actualResponse).isEqualTo(expectedResponse);
	}

	@DisplayName("Given asset with ASSIGNED state then throw exception - negative case")
	@Test
	void editAsset_ShouldThrowsException_whenAssetStateIsAssigned() {
		// given
		String assetCode = "LA100001";
		Asset existAsset = mock(Asset.class);
		AssetState invalidState = AssetState.ASSIGNED;

		EditAssetRequestDto wrongRequest = new EditAssetRequestDto("Dell inspriration 5432", "CPU 7200U, RAM 16GB",
				"2022-01-01", invalidState);

		when(assetRepository.findById(assetCode)).thenReturn(Optional.of(existAsset));
		when(existAsset.getState()).thenReturn(AssetState.ASSIGNED);
		// when
		IllegalStateException exception = Assertions.assertThrows(IllegalStateException.class,
				() -> assetServiceImpl.editAsset(wrongRequest, assetCode));
		// then
		assertThat(exception.getMessage()).isEqualTo("Asset." + assetCode + ".is.being.assigned.Cannot modify");

	}

	@DisplayName("Given non exist asset then throw 404 exception - negative case")
	@Test
	void editAsset_ShouldThrowsException_WhenAssetIsNotExist() {
		// given
		String assetCode = "LA100001";
		when(assetRepository.findById(assetCode)).thenReturn(Optional.empty());
		// when
		ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class,
				() -> assetServiceImpl.editAsset(request, assetCode));

		// then
		assertThat(exception.getMessage()).isEqualTo("Asset." + assetCode + ".not.found");
	}

	// ===========US 579=======
	@DisplayName("Test for get list asset by user but user_id not found")
	@Test
	void getListAsset_ShouldThrownExceptionUserNotFound_WhenUserIdNotExist() {
		List<String> listStates = mock(List.class);
		List<String> listcategories = mock(List.class);
		when(userRepository.findById("SD001")).thenReturn(Optional.empty());
		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			assetServiceImpl.getListAsset("SD001", listcategories, listStates, "keyword", "sortBy", "sortDirection", 1,
					1);
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

		ListAssetResponseDto actual = assetServiceImpl.getListAsset("SD001", listcategories, listStates, "a", "code",
				"DESC", 1, 2);

		ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
		ArgumentCaptor<List> captorlistCategories = ArgumentCaptor.forClass(List.class);
		ArgumentCaptor<List> captorlistStates = ArgumentCaptor.forClass(List.class);

		verify(assetRepository).getListAsset(eq("SD001"), captorlistCategories.capture(), captorlistStates.capture(),
				eq("a"), captor.capture());
		Pageable pageable = captor.getValue();
		
		List<String> listCategoriesExpect = captorlistCategories.getValue();
		List<String> listStatesExpect = captorlistStates.getValue();

		assertThat(listCategoriesExpect.size()).isEqualTo(listcategories.size());
		assertThat(listStatesExpect.size()).isEqualTo(listStates.size());

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
		when(assetRepository.getListAssetByCategory(eq("SD001"), Mockito.any(List.class), eq("a"),
				Mockito.any(Pageable.class))).thenReturn(pageAsset);
		when(pageAsset.getTotalPages()).thenReturn(2);
		when(pageAsset.getContent()).thenReturn(listAsset);
		when(assetMapper.mapperListAsset(listAsset)).thenReturn(expectList);

		ListAssetResponseDto actual = assetServiceImpl.getListAsset("SD001", listcategories, listStates, "a", "code",
				"DESC", 1, 2);

		ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
		ArgumentCaptor<List> captorlist = ArgumentCaptor.forClass(List.class);

		verify(assetRepository).getListAssetByCategory(eq("SD001"), captorlist.capture(), eq("a"), captor.capture());
		Pageable pageable = captor.getValue();

		List<String> listCategoriesExpect = captorlist.getValue();

		assertThat(listCategoriesExpect.size()).isEqualTo(listcategories.size());

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
		when(assetRepository.getListAssetByState(eq("SD001"), Mockito.any(List.class), eq("a"),
				Mockito.any(Pageable.class))).thenReturn(pageAsset);
		when(pageAsset.getTotalPages()).thenReturn(2);
		when(pageAsset.getContent()).thenReturn(listAsset);
		when(assetMapper.mapperListAsset(listAsset)).thenReturn(expectList);

		ListAssetResponseDto actual = assetServiceImpl.getListAsset("SD001", listcategories, listStates, "a", "code",
				"DESC", 1, 2);
		ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
		ArgumentCaptor<List> captorlist = ArgumentCaptor.forClass(List.class);

		verify(assetRepository).getListAssetByState(eq("SD001"), captorlist.capture(), eq("a"), captor.capture());
		Pageable pageable = captor.getValue();
		
		List<String> listStatesExpect = captorlist.getValue();

		assertThat(listStatesExpect.size()).isEqualTo(listStates.size());
		assertThat(pageable.getPageNumber()).isEqualTo(0);
		assertThat(pageable.getPageSize()).isEqualTo(2);
		assertThat(actual.getList()).isEqualTo(expectList);
		assertThat(actual.getTotalPages()).isEqualTo(2);
	}

	//582 - Delete asset
	@Test
	void deleteAsset_ShouldThrowResourceNotFoundException_WhenAssetCodeIncorrect() {
		when(assetRepository.findById("LT1111")).thenReturn(Optional.empty());
		ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class,
				() -> assetServiceImpl.deleteAssetByAssetCode("LT1111"));
		assertThat(exception.getMessage()).isEqualTo("Cannot find asset with asset code: LT1111");
	}

	@Test
	void deleteAsset_ShouldReturnResponseMessage_WhenAssetBelongsToOneOrMoreHistoricalAssignments() {
		Optional<Asset> assetOptional = Optional.of(asset);
		List<Assignment> assignmentList = mock(List.class);
		when(assetRepository.findById("LT1111")).thenReturn(assetOptional);
		when(assignmentRepository.findByAsset(asset)).thenReturn(assignmentList);

		MessageResponse responseMessage = assetServiceImpl.deleteAssetByAssetCode("LT1111");

		assertThat(responseMessage.getMessage()).isEqualTo("Asset belongs to one or more historical assignments");
	}

	@Test
	void deleteAsset_ShouldReturnResponseMessage_DeleteAssetSuccessfully() {
		Optional<Asset> assetOptional = Optional.of(asset);
		when(assetRepository.findById("LT1111")).thenReturn(assetOptional);
		when(assignmentRepository.findByAsset(asset)).thenReturn(Collections.emptyList());

		MessageResponse responseMessage = assetServiceImpl.deleteAssetByAssetCode("LT1111");
		verify(assetRepository).delete(asset);

		assertThat(responseMessage.getMessage()).isEqualTo("Delete asset successfully!");
	}


	@DisplayName("Given page number and page size when get report list then return AssetReportResponseDto Object")
	@Test
	void getAssetReportList_ShouldReturnAssetReportResponseDto_WhenRequestIsValid() {
		//given
		var pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
		var assetPageCaptor = ArgumentCaptor.forClass(Page.class);

		//when
		assetServiceImpl.getAssetReportList(0, 1);

		//then
		verify(assetRepository).getAssetReportList(pageableCaptor.capture());
		assertThat(pageableCaptor.getValue()).isEqualTo(PageRequest.of(0, 1));

		verify(assetMapper).mapToAssetReportDto(assetPageCaptor.capture());
	}
}
