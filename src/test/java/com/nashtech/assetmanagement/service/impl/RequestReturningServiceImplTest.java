package com.nashtech.assetmanagement.service.impl;

import com.nashtech.assetmanagement.dto.request.CreateRequestReturningAssetRequestDto;
import com.nashtech.assetmanagement.dto.response.CreateRequestReturningResponseDto;
import com.nashtech.assetmanagement.entities.*;
import com.nashtech.assetmanagement.exception.ResourceNotFoundException;
import com.nashtech.assetmanagement.mapper.RequestReturningMapper;
import com.nashtech.assetmanagement.repositories.AssetRepository;
import com.nashtech.assetmanagement.repositories.AssignmentRepository;
import com.nashtech.assetmanagement.repositories.RequestReturningRepository;
import com.nashtech.assetmanagement.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static com.nashtech.assetmanagement.enums.RequestReturningState.WAITING_FOR_RETURNING;
import static com.nashtech.assetmanagement.utils.AppConstants.ACCEPTED;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

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
}
