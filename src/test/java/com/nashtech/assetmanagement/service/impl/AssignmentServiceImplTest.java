package com.nashtech.assetmanagement.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nashtech.assetmanagement.dto.request.RequestAssignmentDTO;
import com.nashtech.assetmanagement.dto.response.AssignmentDto;
import com.nashtech.assetmanagement.entities.Asset;
import com.nashtech.assetmanagement.entities.Assignment;
import com.nashtech.assetmanagement.entities.Users;
import com.nashtech.assetmanagement.enums.AssetState;
import com.nashtech.assetmanagement.exception.ResourceNotFoundException;
import com.nashtech.assetmanagement.mapper.AssignmentContent;
import com.nashtech.assetmanagement.mapper.AssignmentMapper;
import com.nashtech.assetmanagement.repositories.AssetRepository;
import com.nashtech.assetmanagement.repositories.AssignmentRepository;
import com.nashtech.assetmanagement.repositories.UserRepository;

class AssignmentServiceImplTest {

    AssignmentRepository assignmentRepository;
    AssignmentContent assignmentContent;
    UserRepository userRepository;
    AssetRepository assetRepository;
    AssignmentMapper assignmentMapper;
    AssignmentServiceImpl assignmentServiceImpl;

    @BeforeEach
    void setUp() {
        assignmentRepository = mock(AssignmentRepository.class);
        assignmentContent = mock(AssignmentContent.class);
        userRepository = mock(UserRepository.class);
        assetRepository = mock(AssetRepository.class);
        assignmentMapper = mock(AssignmentMapper.class);
        assignmentServiceImpl = new AssignmentServiceImpl(assignmentRepository, assignmentContent, userRepository, assetRepository, assignmentMapper);

    }

//    @Test
//    void givenStateAndTextAndDate_whenGetAssignmentsByCondition_thenReturnListAssignmentResponse() {
//        //given
//        String[] arrayState = new String[] {"accepted" , "declined"};
//
//        List<String> states = Arrays.asList(arrayState);
//        List<String> assignmentState = states.stream()
//                .map(StateConverter::getAssignmentState)
//                .collect(Collectors.toList());
//
//        assertThat(assignmentState.get(0)).isEqualTo("Accepted");
//        assertThat(assignmentState.get(1)).isEqualTo("Declined");
//        assertThat(assignmentState.size()).isEqualTo(2);
//
//        Pageable pageable = PageRequest.of(0,1);
//        Page<Assignment> assignmentPage = mock(Page.class);
//        Date assignedDate = mock(Date.class);
//        String assignedDateStr = "2022-06-07";
//        assignedDate = Date.valueOf(assignedDateStr);
//        Date dateNow = new Date(new java.util.Date().getTime());
//
//    }


    //US584 Create New Assignment
    @Test
    void createNewAssignment_ShouldReturnResponseAssignmentDto_WhenRequestValid() {
        RequestAssignmentDTO request = mock(RequestAssignmentDTO.class);
        when(request.getAssignedTo()).thenReturn("assignTo");
        when(request.getAssignedBy()).thenReturn("assignBy");
        when(request.getAssetCode()).thenReturn("assetCode");
        Asset asset = mock(Asset.class);
        Users assignBy = mock(Users.class);
        Users assignTo = mock(Users.class);
        Assignment assignment = mock(Assignment.class);
        AssignmentDto response = mock(AssignmentDto.class);
        when(assetRepository.findById("assetCode")).thenReturn(Optional.of(asset));
        when(userRepository.findById("assignBy")).thenReturn(Optional.of(assignBy));
        when(userRepository.findById("assignTo")).thenReturn(Optional.of(assignTo));
        when(assignmentMapper.MapRequestAssignmentToAssignment(request)).thenReturn(assignment);
        when(assignmentMapper.MapAssignmentToResponseDto(assignment)).thenReturn(response);
        AssignmentDto result = assignmentServiceImpl.createNewAssignment(request);
        verify(asset).setState(AssetState.ASSIGNED);
        verify(assignment).setAssignedTo(assignTo);
        verify(assignment).setAssignedBy(assignBy);
        verify(assignment).setAsset(asset);
        verify(assignmentRepository).save(assignment);
        assertThat(result).isEqualTo(response);
    }

    @Test
    void createNewAssignment_ShouldThrowResourceNotFoundEx_WhenAssetCodeNotExist(){
        Asset asset = mock(Asset.class);
        RequestAssignmentDTO request = mock(RequestAssignmentDTO.class);
        when(assetRepository.findById("assetCode")).thenReturn(Optional.empty());
        ResourceNotFoundException e = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> assignmentServiceImpl.createNewAssignment(request));
        assertThat(e.getMessage()).isEqualTo("AssetCode not found");
    }

    @Test
    void createNewAssignment_ShouldThrowResourceNotFoundEx_WhenUserAssignToNotExist(){
        Asset asset = mock(Asset.class);
        RequestAssignmentDTO request = mock(RequestAssignmentDTO.class);
        when(request.getAssetCode()).thenReturn("assetCode");
        when(request.getAssignedTo()).thenReturn("assignTo");
        when(assetRepository.findById("assetCode")).thenReturn(Optional.of(asset));
        when(userRepository.findById("assignTo")).thenReturn(Optional.empty());
        ResourceNotFoundException e = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> assignmentServiceImpl.createNewAssignment(request));
        assertThat(e.getMessage()).isEqualTo("Assign to User not found");
    }

    @Test
    void createNewAssignment_ShouldThrowResourceNotFoundEx_WhenUserAssignByNotExist(){
        Asset asset = mock(Asset.class);
        Users assignTo = mock(Users.class);
        RequestAssignmentDTO request = mock(RequestAssignmentDTO.class);
        when(request.getAssetCode()).thenReturn("assetCode");
        when(request.getAssignedTo()).thenReturn("assignTo");
        when(request.getAssignedBy()).thenReturn("assignBy");
        when(assetRepository.findById("assetCode")).thenReturn(Optional.of(asset));
        when(userRepository.findById("assignTo")).thenReturn(Optional.of(assignTo));
        when(userRepository.findById("assignBy")).thenReturn(Optional.empty());
        ResourceNotFoundException e = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> assignmentServiceImpl.createNewAssignment(request));
        assertThat(e.getMessage()).isEqualTo("Assign by User not found");
    }

    @Test
    void getListAssignmentByAsset_ShouldReturnListAssignmentDto_WhenAssetIdExist() {
    	Asset entity = mock(Asset.class);
		List<Assignment> listEntity = mock(List.class);
		List<AssignmentDto> expected = mock(List.class);
    	when(assetRepository.findById("Laptop001")).thenReturn(Optional.of(entity));
    	when(assignmentRepository.findByAsset(entity)).thenReturn(listEntity);
    	when(assignmentMapper.mapperListAssignment(listEntity)).thenReturn(expected);
    	
    	List<AssignmentDto> actual = assignmentServiceImpl.getListAssignmentByAssetCode("Laptop001");
    	assertThat(actual).isEqualTo(expected);
    }
    
    @Test
    void getListAssignmentByAsset_ShouldReturnListAssignmentDto_WhenAssetIdNotExist() {
    	when(assetRepository.findById("Laptop001")).thenReturn(Optional.empty());
    	Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
    		assignmentServiceImpl.getListAssignmentByAssetCode("Laptop001");
		});
    	assertThat(exception.getMessage()).isEqualTo("asset.not.found.with.code:Laptop001");
    }
}