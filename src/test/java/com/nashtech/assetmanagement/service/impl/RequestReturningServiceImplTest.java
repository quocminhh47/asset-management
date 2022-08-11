package com.nashtech.assetmanagement.service.impl;

import static com.nashtech.assetmanagement.enums.RequestReturningState.WAITING_FOR_RETURNING;
import static com.nashtech.assetmanagement.utils.AppConstants.ACCEPTED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.nashtech.assetmanagement.dto.request.CreateRequestReturningAssetRequestDto;
import com.nashtech.assetmanagement.dto.request.RequestReturningRequestGetListDto;
import com.nashtech.assetmanagement.dto.response.CreateRequestReturningResponseDto;
import com.nashtech.assetmanagement.dto.response.ListRequestReturningResponseDto;
import com.nashtech.assetmanagement.dto.response.RequestReturningResponseDto;
import com.nashtech.assetmanagement.entities.Asset;
import com.nashtech.assetmanagement.entities.Assignment;
import com.nashtech.assetmanagement.entities.AssignmentId;
import com.nashtech.assetmanagement.entities.RequestReturning;
import com.nashtech.assetmanagement.entities.Users;
import com.nashtech.assetmanagement.enums.RequestReturningState;
import com.nashtech.assetmanagement.exception.ResourceNotFoundException;
import com.nashtech.assetmanagement.mapper.RequestReturningMapper;
import com.nashtech.assetmanagement.repositories.AssetRepository;
import com.nashtech.assetmanagement.repositories.AssignmentRepository;
import com.nashtech.assetmanagement.repositories.RequestReturningRepository;
import com.nashtech.assetmanagement.repositories.UserRepository;

public class RequestReturningServiceImplTest {
    private UserRepository userRepository;
    private AssetRepository assetRepository;
    private AssignmentRepository assignmentRepository;
    private ModelMapper modelMapper;
    private RequestReturningRepository requestReturningRepository;
    private RequestReturningServiceImpl requestReturningServiceImpl;
    private RequestReturningMapper requestReturningMapper;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        assetRepository = mock(AssetRepository.class);
        assignmentRepository = mock(AssignmentRepository.class);
        modelMapper = mock(ModelMapper.class);
        requestReturningRepository = mock(RequestReturningRepository.class);
        requestReturningMapper = mock(RequestReturningMapper.class);
        requestReturningServiceImpl = new RequestReturningServiceImpl(
                userRepository,
                assetRepository,
                assignmentRepository,
                modelMapper,
                requestReturningRepository,
                requestReturningMapper);
    }

    @DisplayName("Given invalid requestedBy username when create request returning asset then return exception - negative case")
    @Test
    public void createRequestReturningAsset_ShouldReturnResourceNotFoundException_WhenRequestedByUsernameInvalid() {
        CreateRequestReturningAssetRequestDto createRequestReturningAssetRequestDto = mock(CreateRequestReturningAssetRequestDto.class);

        when(createRequestReturningAssetRequestDto.getRequestedBy()).thenReturn("longt");
        when(userRepository.findByUserName("longt")).thenReturn(Optional.empty());
        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> requestReturningServiceImpl.createRequestReturningAsset(createRequestReturningAssetRequestDto));

        assertThat(exception.getMessage()).isEqualTo("Cannot find requestedByUser with username: longt");
    }

    @DisplayName("Given invalid assetCode when create request returning asset then return exception - negative case")
    @Test
    public void createRequestReturningAsset_ShouldReturnResourceNotFoundException_WhenAssetCodeInvalid() {
        CreateRequestReturningAssetRequestDto createRequestReturningAssetRequestDto = mock(CreateRequestReturningAssetRequestDto.class);
        Users requestedByUser = mock(Users.class);

        when(createRequestReturningAssetRequestDto.getRequestedBy()).thenReturn("longt");
        when(userRepository.findByUserName("longt")).thenReturn(Optional.of(requestedByUser));
        when(createRequestReturningAssetRequestDto.getAssetCode()).thenReturn("CT123456");
        when(assetRepository.findById("CT123456")).thenReturn(Optional.empty());
        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> requestReturningServiceImpl.createRequestReturningAsset(createRequestReturningAssetRequestDto));

        assertThat(exception.getMessage()).isEqualTo("Cannot find asset with asset code: CT123456");
    }

    @DisplayName("Given invalid assignmentId when create request returning asset then return exception - negative case")
    @Test
    public void createRequestReturningAsset_ShouldReturnResourceNotFoundException_WhenAssignmentIdInvalid() {
        CreateRequestReturningAssetRequestDto createRequestReturningAssetRequestDto = mock(CreateRequestReturningAssetRequestDto.class);
        Users requestedByUser = mock(Users.class);
        Asset asset = mock(Asset.class);

        when(createRequestReturningAssetRequestDto.getRequestedBy()).thenReturn("longt");
        when(userRepository.findByUserName("longt")).thenReturn(Optional.of(requestedByUser));
        when(createRequestReturningAssetRequestDto.getAssetCode()).thenReturn("CT123456");
        when(assetRepository.findById("CT123456")).thenReturn(Optional.of(asset));
        ArgumentCaptor<AssignmentId> assignmentIdArgumentCaptor = ArgumentCaptor.forClass(AssignmentId.class);
        when(assignmentRepository.findById(assignmentIdArgumentCaptor.capture())).thenReturn(Optional.empty());
        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> requestReturningServiceImpl.createRequestReturningAsset(createRequestReturningAssetRequestDto));

        assertThat(exception.getMessage()).isEqualTo("Cannot find assignment with assignment id: " + assignmentIdArgumentCaptor.getValue());
    }

    @DisplayName("Given assignment state is not accepted when create request returning asset then return exception - negative case")
    @Test
    public void createRequestReturningAsset_ShouldReturnRuntimeException_WhenAssignmentStateIsNotAccepted() {
        CreateRequestReturningAssetRequestDto createRequestReturningAssetRequestDto = mock(CreateRequestReturningAssetRequestDto.class);
        Users requestedByUser = mock(Users.class);
        Asset asset = mock(Asset.class);
        Assignment assignment = mock(Assignment.class);

        when(createRequestReturningAssetRequestDto.getRequestedBy()).thenReturn("longt");
        when(userRepository.findByUserName("longt")).thenReturn(Optional.of(requestedByUser));
        when(createRequestReturningAssetRequestDto.getAssetCode()).thenReturn("CT123456");
        when(assetRepository.findById("CT123456")).thenReturn(Optional.of(asset));
        ArgumentCaptor<AssignmentId> assignmentIdArgumentCaptor = ArgumentCaptor.forClass(AssignmentId.class);
        when(assignmentRepository.findById(assignmentIdArgumentCaptor.capture())).thenReturn(Optional.of(assignment));
        // Assignment state is not 'Accepted'
        when(assignment.getState()).thenReturn("Declined");

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class,
                () -> requestReturningServiceImpl.createRequestReturningAsset(createRequestReturningAssetRequestDto));

        assertThat(exception.getMessage()).isEqualTo("Request returning is only enabled for assignments have state is Accepted");
    }

    @DisplayName("Given request returning have more than one assignment when create request returning asset then return exception - negative case")
    @Test
    public void createRequestReturningAsset_ShouldReturnRuntimeException_WhenRequestReturningHaveMoreThanOneAssignment() {
        CreateRequestReturningAssetRequestDto createRequestReturningAssetRequestDto = mock(CreateRequestReturningAssetRequestDto.class);
        Users requestedByUser = mock(Users.class);
        Asset asset = mock(Asset.class);
        Assignment assignment = mock(Assignment.class);
        RequestReturning requestReturning = mock(RequestReturning.class);

        when(createRequestReturningAssetRequestDto.getRequestedBy()).thenReturn("longt");
        when(userRepository.findByUserName("longt")).thenReturn(Optional.of(requestedByUser));
        when(createRequestReturningAssetRequestDto.getAssetCode()).thenReturn("CT123456");
        when(assetRepository.findById("CT123456")).thenReturn(Optional.of(asset));
        ArgumentCaptor<AssignmentId> assignmentIdArgumentCaptor = ArgumentCaptor.forClass(AssignmentId.class);
        when(assignmentRepository.findById(assignmentIdArgumentCaptor.capture())).thenReturn(Optional.of(assignment));
        when(assignment.getState()).thenReturn("Accepted");
        when(requestReturningRepository.getRequestReturningByAssignment(assignment)).thenReturn(Optional.of(requestReturning));
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class,
                () -> requestReturningServiceImpl.createRequestReturningAsset(createRequestReturningAssetRequestDto));

        assertThat(exception.getMessage()).isEqualTo("One assignment must have only one request returning");
    }

    @DisplayName("Given valid createRequestReturningAssetRequestDto when create request returning asset then return CreateRequestReturningResponseDto - positive case")
    @Test
    public void createRequestReturningAsset_ShouldReturnCreateRequestReturningResponseDto_WhenCreateRequestReturningAssetRequestDtoValid() {
        CreateRequestReturningAssetRequestDto createRequestReturningAssetRequestDto = mock(CreateRequestReturningAssetRequestDto.class);
        Users requestedByUser = mock(Users.class);
        Asset asset = mock(Asset.class);
        Assignment assignment = mock(Assignment.class);

        when(createRequestReturningAssetRequestDto.getRequestedBy()).thenReturn("longt");
        when(userRepository.findByUserName("longt")).thenReturn(Optional.of(requestedByUser));

        when(createRequestReturningAssetRequestDto.getAssetCode()).thenReturn("CT123456");
        when(assetRepository.findById("CT123456")).thenReturn(Optional.of(asset));

        var assignmentIdArgumentCaptor = ArgumentCaptor.forClass(AssignmentId.class);

        when(assignmentRepository.findById(assignmentIdArgumentCaptor.capture())).thenReturn(Optional.of(assignment));
        when(assignment.getState()).thenReturn(ACCEPTED);

        when(requestReturningRepository.getRequestReturningByAssignment(assignment)).thenReturn(Optional.empty());
        var requestReturningArgumentCaptor = ArgumentCaptor.forClass(RequestReturning.class);

        CreateRequestReturningResponseDto createRequestReturningResponseDtoExpected
                = requestReturningServiceImpl.createRequestReturningAsset(createRequestReturningAssetRequestDto);

        verify(requestReturningRepository).save(requestReturningArgumentCaptor.capture());

        RequestReturning returningValue = requestReturningArgumentCaptor.getValue();
        assertThat(returningValue.getRequestedBy()).isEqualTo(requestedByUser);
        assertThat(returningValue.getAssignment()).isEqualTo(assignment);
        assertThat(returningValue.getReturnedDate()).isEqualTo(createRequestReturningAssetRequestDto.getReturnedDate());
        assertThat(returningValue.getState()).isEqualTo(WAITING_FOR_RETURNING);

        verify(modelMapper).map(returningValue, CreateRequestReturningResponseDto.class);
    }

	@Test
	void getListRequestReturning_ShouldReturnListRequestReturningResponseDto_whenTheRequestIsValid() {
		List<String> listStatesActual = List.of("COMPLETED", "WAITING_FOR_RETURNING");
		RequestReturningRequestGetListDto dtoValue = new RequestReturningRequestGetListDto(listStatesActual,
				"2022-08-09", "a", "assignment.asset.code", "ASC", 1, 2);

		RequestReturningRequestGetListDto dto = mock(RequestReturningRequestGetListDto.class);
		Page<RequestReturning> pageRequestReturning = mock(Page.class);
		List<RequestReturning> listEntity = mock(List.class);
		List<RequestReturningResponseDto> expectList = mock(List.class);
		List<RequestReturningState> requestReturningState = mock(List.class);
		List<String> listStates = mock(List.class);
		
		when(requestReturningMapper.mapperListStates(dto.getStates())).thenReturn(requestReturningState);
		when(requestReturningRepository.getListRequestReturning(Mockito.any(List.class), Mockito.any(Date.class),
				eq("a"), Mockito.any(Pageable.class))).thenReturn(pageRequestReturning);
		when(pageRequestReturning.getTotalPages()).thenReturn(2);
		when(pageRequestReturning.getContent()).thenReturn(listEntity);

		when(requestReturningMapper.mapperListRequestReturning(listEntity)).thenReturn(expectList);

		ListRequestReturningResponseDto actual = requestReturningServiceImpl.getListRequestReturning(dtoValue);

		ArgumentCaptor<List> captorlistStates = ArgumentCaptor.forClass(List.class);
		ArgumentCaptor<Date> captorDate = ArgumentCaptor.forClass(Date.class);
		ArgumentCaptor<Pageable> captorPageable = ArgumentCaptor.forClass(Pageable.class);

		verify(requestReturningRepository).getListRequestReturning(captorlistStates.capture(), captorDate.capture(),
				eq("a"), captorPageable.capture());

		Pageable pageable = captorPageable.getValue();
		Date dateExpect = captorDate.getValue();
		List<String> listStatesExpect = captorlistStates.getValue();

		assertThat(listStatesExpect.size()).isEqualTo(listStates.size());
		assertThat(dateExpect).isEqualTo(dtoValue.getReturnedDate());

		assertThat(pageable.getPageNumber()).isEqualTo(0);
		assertThat(pageable.getPageSize()).isEqualTo(2);
		assertThat(actual.getList()).isEqualTo(expectList);
		assertThat(actual.getTotalPages()).isEqualTo(2);
		assertThat(pageable.getSort().ascending()).isEqualTo(Sort.by("assignment.asset.code"));
		
	}

	@Test
	void getListRequestReturning_ShouldReturnListRequestReturningResponseDto_whenTheRequestLackOfReturnDate() {

		List<String> listStatesActual = new ArrayList<String>();
		listStatesActual.add("COMPLETED");
		listStatesActual.add("WAITING_FOR_RETURNING");
		RequestReturningRequestGetListDto dtoValue = new RequestReturningRequestGetListDto(listStatesActual, "", "a",
				"id", "ASC", 1, 2);

		RequestReturningRequestGetListDto dto = mock(RequestReturningRequestGetListDto.class);
		Page<RequestReturning> pageRequestReturning = mock(Page.class);
		List<RequestReturning> listEntity = mock(List.class);
		List<RequestReturningResponseDto> expectList = mock(List.class);
		List<RequestReturningState> requestReturningState = mock(List.class);
		List<String> listStates = mock(List.class);

		when(requestReturningMapper.mapperListStates(dto.getStates())).thenReturn(requestReturningState);
		when(requestReturningRepository.getListRequestReturningByStates(Mockito.any(List.class), eq("a"),
				Mockito.any(Pageable.class))).thenReturn(pageRequestReturning);
		when(pageRequestReturning.getTotalPages()).thenReturn(2);
		when(pageRequestReturning.getContent()).thenReturn(listEntity);

		when(requestReturningMapper.mapperListRequestReturning(listEntity)).thenReturn(expectList);

		ListRequestReturningResponseDto actual = requestReturningServiceImpl.getListRequestReturning(dtoValue);

		ArgumentCaptor<List> captorlistStates = ArgumentCaptor.forClass(List.class);
		ArgumentCaptor<Pageable> captorPageable = ArgumentCaptor.forClass(Pageable.class);

		verify(requestReturningRepository).getListRequestReturningByStates(captorlistStates.capture(), eq("a"),
				captorPageable.capture());

		Pageable pageable = captorPageable.getValue();
		List<String> listStatesExpect = captorlistStates.getValue();

		assertThat(listStatesExpect.size()).isEqualTo(listStates.size());
		assertThat(dtoValue.getReturnedDate()).isEqualTo("");

		assertThat(pageable.getPageNumber()).isEqualTo(0);
		assertThat(pageable.getPageSize()).isEqualTo(2);
		assertThat(actual.getList()).isEqualTo(expectList);
		assertThat(actual.getTotalPages()).isEqualTo(2);

		assertThat(pageable.getSort().ascending()).isEqualTo(Sort.by("id"));
	}

	@Test
	void getListRequestReturning_ShouldThrowsDateInvalidException_whenTheRequestLackOfReturnDate() {
		List<String> listStatesActual = new ArrayList<String>();
		listStatesActual.add("COMPLETED");
		listStatesActual.add("WAITING_FOR_RETURNING");
		RequestReturningRequestGetListDto dtoValue = new RequestReturningRequestGetListDto(listStatesActual,
				"2022/08/99", "a", "id", "DESC", 1, 2);

		RequestReturningRequestGetListDto dto = mock(RequestReturningRequestGetListDto.class);
		List<RequestReturningState> requestReturningState = mock(List.class);
		when(requestReturningMapper.mapperListStates(dto.getStates())).thenReturn(requestReturningState);

		Exception exception = assertThrows(Exception.class, () -> {
			requestReturningServiceImpl.getListRequestReturning(dtoValue);
		});

		assertThat(exception.getMessage()).isEqualTo("Date.format.is.not.valid");
	}
}
