package com.nashtech.assetmanagement.service.impl;


import com.nashtech.assetmanagement.dto.request.CreateRequestReturningAssetRequestDto;
import com.nashtech.assetmanagement.dto.request.RequestReturningRequestGetListDto;
import com.nashtech.assetmanagement.dto.request.ReturningRequestDto;
import com.nashtech.assetmanagement.dto.response.CreateRequestReturningResponseDto;
import com.nashtech.assetmanagement.dto.response.ListRequestReturningResponseDto;
import com.nashtech.assetmanagement.dto.response.MessageResponse;
import com.nashtech.assetmanagement.dto.response.RequestReturningResponseDto;
import com.nashtech.assetmanagement.entities.*;
import com.nashtech.assetmanagement.enums.AssetState;
import com.nashtech.assetmanagement.enums.RequestReturningState;
import com.nashtech.assetmanagement.exception.BadRequestException;
import com.nashtech.assetmanagement.exception.RequestNotAcceptException;
import com.nashtech.assetmanagement.exception.ResourceNotFoundException;
import com.nashtech.assetmanagement.mapper.RequestReturningMapper;
import com.nashtech.assetmanagement.repositories.AssetRepository;
import com.nashtech.assetmanagement.repositories.AssignmentRepository;
import com.nashtech.assetmanagement.repositories.RequestReturningRepository;
import com.nashtech.assetmanagement.repositories.UserRepository;
import com.nashtech.assetmanagement.service.AuthenticationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.nashtech.assetmanagement.enums.RequestReturningState.WAITING_FOR_RETURNING;
import static com.nashtech.assetmanagement.service.impl.RequestReturningServiceImpl.INVALID_STATE;
import static com.nashtech.assetmanagement.utils.AppConstants.ACCEPTED;
import static com.nashtech.assetmanagement.utils.AppConstants.DONE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class RequestReturningServiceImplTest {

    @Mock
    UserRepository userRepository;
    @Mock
    RequestReturningRepository requestReturningRepository;
    @Mock
    AssetRepository assetRepository;
    @Mock
    RequestReturningMapper requestReturningMapper;
    @Mock
    AssignmentRepository assignmentRepository;
    @Mock
    ModelMapper modelMapper;
    @Mock
    AuthenticationService authenticationService;
    @InjectMocks
    RequestReturningServiceImpl requestReturningServiceImpl;

    Assignment assignment;
    RequestReturning returningRequest;
    RequestReturning savedReturningRequest;
    Asset asset;
    Users acceptedUser;
    RequestReturningResponseDto expectedResponse;

    private RequestReturning requestReturning;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        assignment = mock(Assignment.class);
        returningRequest = mock(RequestReturning.class);
        savedReturningRequest = mock(RequestReturning.class);
        asset = mock(Asset.class);
        acceptedUser = mock(Users.class);
        expectedResponse = mock(RequestReturningResponseDto.class);
        requestReturning=mock(RequestReturning.class);

    }

    @DisplayName("Given all request is valid when complete returning request then " +
            "return RequestReturningResponseDto - positive case")
    @Test
    void completeReturningRequest_ShouldReturnRequestReturningResponseDto_WhenTheRequestIsValid() {
        //given
        ReturningRequestDto requestDto =
                new ReturningRequestDto("SD0001", "LA100005", "2022-08-11");
        var assignmentIdCaptor = ArgumentCaptor.forClass(AssignmentId.class);
        var returnDateCaptor = ArgumentCaptor.forClass(Date.class);

        when(assignmentRepository.findById(any(AssignmentId.class))).thenReturn(Optional.of(assignment));
        when(assignment.getRequestReturning()).thenReturn(returningRequest);
        when(returningRequest.getState()).thenReturn(RequestReturningState.WAITING_FOR_RETURNING);
        when(assignment.getAsset()).thenReturn(asset);
        when(authenticationService.getUser()).thenReturn(acceptedUser);
        when(requestReturningRepository.save(any(RequestReturning.class))).thenReturn(savedReturningRequest);
        when(modelMapper.map(savedReturningRequest, RequestReturningResponseDto.class)).thenReturn(expectedResponse);

        //when
        RequestReturningResponseDto actualResponse = requestReturningServiceImpl.completeReturningRequest(requestDto);

        //then
        verify(assignmentRepository).findById(assignmentIdCaptor.capture());
        AssignmentId assignmentIdValue = assignmentIdCaptor.getValue();
        assertThat(assignmentIdValue.getAssignedTo()).isEqualTo(requestDto.getAssignedTo());
        assertThat(assignmentIdValue.getAssetCode()).isEqualTo(requestDto.getAssetCode());
        assertThat(assignmentIdValue.getAssignedDate()).isEqualTo(Date.valueOf(requestDto.getAssignedDate()));

        verify(assignment).setState(DONE);
        verify(returningRequest).setState(RequestReturningState.COMPLETED);
        verify(returningRequest).setAssignment(assignment);

        verify(returningRequest).setReturnedDate(returnDateCaptor.capture());
        assertThat(returnDateCaptor.getValue()).isEqualTo(Date.valueOf(LocalDate.now()));

        verify(returningRequest).setAcceptedBy(acceptedUser);
        verify(asset).setState(AssetState.AVAILABLE);
        verify(assetRepository).save(asset);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @DisplayName("Given request with invalid assigned date format then throws exception - negative case")
    @Test
    void completeReturningRequest_WhenAssignedDateFormatInvalid_ThenThrowsException() {
        //given
        ReturningRequestDto requestDto =
                new ReturningRequestDto("SD0001", "LA100005", "202208-11");

        //when
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> requestReturningServiceImpl.completeReturningRequest(requestDto));
        //then
        assertThat(exception.getMessage()).isEqualTo("Assigned date format is not valid !");
    }


    @DisplayName("Given non exist assignment ID when complete returning request then throws exception")
    @Test
    void completeReturningRequest_WhenAssignmentIsNonExist_ThenThrowsException() {
        //given
        ReturningRequestDto requestDto =
                new ReturningRequestDto("SD0001", "LA100005", "2022-08-11");
        var assignmentIdCaptor = ArgumentCaptor.forClass(AssignmentId.class);
        when(assignmentRepository.findById(assignmentIdCaptor.capture())).thenReturn(Optional.empty());

        //when
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> requestReturningServiceImpl.completeReturningRequest(requestDto));

        //then
        verify(assignmentRepository).findById(assignmentIdCaptor.capture());
        AssignmentId assignmentIdValue = assignmentIdCaptor.getValue();
        assertThat(assignmentIdValue.getAssignedTo()).isEqualTo(requestDto.getAssignedTo());
        assertThat(assignmentIdValue.getAssetCode()).isEqualTo(requestDto.getAssetCode());

        assertThat(exception.getMessage()).isEqualTo("Assignment is not exist !");
    }

    @DisplayName("Given returning request with state COMPLETED when complete returning request " +
            "then throws exception")
    @Test
    void completeReturningRequest_WhenReturningRequestNonAcceptable_ThenThrowsException() {
        //given
        ReturningRequestDto requestDto =
                new ReturningRequestDto("SD0001", "LA100005", "2022-08-11");
        var assignmentIdCaptor = ArgumentCaptor.forClass(AssignmentId.class);
        when(assignmentRepository.findById(assignmentIdCaptor.capture())).thenReturn(Optional.of(assignment));
        when(assignment.getRequestReturning()).thenReturn(returningRequest);
        when(returningRequest.getState()).thenReturn(RequestReturningState.COMPLETED);

        //when

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> requestReturningServiceImpl.completeReturningRequest(requestDto));

        // then
        verify(assignmentRepository).findById(assignmentIdCaptor.capture());
        AssignmentId assignmentIdValue = assignmentIdCaptor.getValue();
        assertThat(assignmentIdValue.getAssignedTo()).isEqualTo(requestDto.getAssignedTo());
        assertThat(assignmentIdValue.getAssetCode()).isEqualTo(requestDto.getAssetCode());
        assertThat(assignmentIdValue.getAssignedDate()).isEqualTo(Date.valueOf(requestDto.getAssignedDate()));

        assertThat(exception.getMessage()).isEqualTo(INVALID_STATE);

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

    @DisplayName("Given invalid assignedTo username when create request returning asset then return exception - negative case")
    @Test
    public void createRequestReturningAsset_ShouldReturnResourceNotFoundException_WhenAssignedToUsernameInvalid() {
        CreateRequestReturningAssetRequestDto createRequestReturningAssetRequestDto = mock(CreateRequestReturningAssetRequestDto.class);
        Users requestedByUser = mock(Users.class);
        when(createRequestReturningAssetRequestDto.getRequestedBy()).thenReturn("longt");
        when(userRepository.findByUserName("longt")).thenReturn(Optional.of(requestedByUser));
        when(createRequestReturningAssetRequestDto.getAssignedTo()).thenReturn("longt2");
        when(userRepository.findByUserName("longt2")).thenReturn(Optional.empty());
        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> requestReturningServiceImpl.createRequestReturningAsset(createRequestReturningAssetRequestDto));

        assertThat(exception.getMessage()).isEqualTo("Cannot find assignedToUser with username: longt2");
    }

    @DisplayName("Given invalid assetCode when create request returning asset then return exception - negative case")
    @Test
    public void createRequestReturningAsset_ShouldReturnResourceNotFoundException_WhenAssetCodeInvalid() {
        CreateRequestReturningAssetRequestDto createRequestReturningAssetRequestDto = mock(CreateRequestReturningAssetRequestDto.class);
        Users requestedByUser = mock(Users.class);
        Users assignedToUser = mock(Users.class);


        when(createRequestReturningAssetRequestDto.getRequestedBy()).thenReturn("longt");
        when(userRepository.findByUserName("longt")).thenReturn(Optional.of(requestedByUser));
        when(createRequestReturningAssetRequestDto.getAssignedTo()).thenReturn("longt2");
        when(userRepository.findByUserName("longt2")).thenReturn(Optional.of(assignedToUser));
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
        Users assignedToUser = mock(Users.class);
        Asset asset = mock(Asset.class);

        when(createRequestReturningAssetRequestDto.getRequestedBy()).thenReturn("longt");
        when(userRepository.findByUserName("longt")).thenReturn(Optional.of(requestedByUser));
        when(createRequestReturningAssetRequestDto.getAssignedTo()).thenReturn("longt2");
        when(userRepository.findByUserName("longt2")).thenReturn(Optional.of(assignedToUser));
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
        Users assignedToUser = mock(Users.class);
        Asset asset = mock(Asset.class);
        Assignment assignment = mock(Assignment.class);

        when(createRequestReturningAssetRequestDto.getRequestedBy()).thenReturn("longt");
        when(userRepository.findByUserName("longt")).thenReturn(Optional.of(requestedByUser));
        when(createRequestReturningAssetRequestDto.getAssignedTo()).thenReturn("longt2");
        when(userRepository.findByUserName("longt2")).thenReturn(Optional.of(assignedToUser));
        when(createRequestReturningAssetRequestDto.getAssetCode()).thenReturn("CT123456");
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
        Users assignedToUser = mock(Users.class);
        Asset asset = mock(Asset.class);
        Assignment assignment = mock(Assignment.class);
        RequestReturning requestReturning = mock(RequestReturning.class);

        when(createRequestReturningAssetRequestDto.getRequestedBy()).thenReturn("longt");
        when(userRepository.findByUserName("longt")).thenReturn(Optional.of(requestedByUser));
        when(createRequestReturningAssetRequestDto.getAssignedTo()).thenReturn("longt2");
        when(userRepository.findByUserName("longt2")).thenReturn(Optional.of(assignedToUser));
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
        Users assignedToUser = mock(Users.class);
        Asset asset = mock(Asset.class);
        Assignment assignment = mock(Assignment.class);
        CreateRequestReturningResponseDto createRequestReturningResponseDtoActual = mock(CreateRequestReturningResponseDto.class);

        when(createRequestReturningAssetRequestDto.getRequestedBy()).thenReturn("longt");
        when(userRepository.findByUserName("longt")).thenReturn(Optional.of(requestedByUser));
        when(createRequestReturningAssetRequestDto.getAssignedTo()).thenReturn("longt2");
        when(userRepository.findByUserName("longt2")).thenReturn(Optional.of(assignedToUser));

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
        assertThat(returningValue.getState()).isEqualTo(WAITING_FOR_RETURNING);

        when(modelMapper.map(returningValue, CreateRequestReturningResponseDto.class)).thenReturn(createRequestReturningResponseDtoActual);

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
    void cancelRequestReturningAssignment_ShouldReturnSuccessMessage_WhenRequestValid() {
        when(requestReturningRepository.findById(1L)).thenReturn(Optional.of(requestReturning));
        when(requestReturning.getState()).thenReturn(WAITING_FOR_RETURNING);
        MessageResponse expected = mock(MessageResponse.class);
        when(expected.getStatus()).thenReturn(HttpStatus.OK);
        when(expected.getMessage()).thenReturn("Cancel successfully!");
        MessageResponse actual =
                requestReturningServiceImpl.cancelRequestReturningAssignment(1L);
        verify(requestReturningRepository).delete(requestReturning);
        assertThat(actual.getTimeStamp()).isNotNull();
        assertThat(actual.getStatus()).isEqualTo(expected.getStatus());
        assertThat(actual.getMessage()).isEqualTo(expected.getMessage());
    }

    @Test
    void cancelRequestReturningAssignment_ShouldThrowResourceNotFoundException_WhenCannotFindRequestReturning() {
        when(requestReturningRepository.findById(1L)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> requestReturningServiceImpl.cancelRequestReturningAssignment(1L));
        assertThat(exception.getMessage()).isEqualTo("Cannot find request returning " +
                "with id: " + 1L);
    }

    @Test
    void cancelRequestReturningAssignment_ShouldThrowRequestNotAcceptException_WhenStateIsNotWaitingForReturning() {
        when(requestReturningRepository.findById(1L)).thenReturn(Optional.of(requestReturning));
        when(requestReturning.getState()).thenReturn(RequestReturningState.COMPLETED);
        RequestNotAcceptException exception = Assertions.assertThrows(RequestNotAcceptException.class,
                () -> requestReturningServiceImpl.cancelRequestReturningAssignment(1L));
        assertThat(exception.getMessage()).isEqualTo("Only request with state 'Waiting for " +
                "returning' can be deleted");
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

