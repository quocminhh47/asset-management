package com.nashtech.assetmanagement.service.impl;

import com.nashtech.assetmanagement.dto.request.RequestAssignmentDTO;
import com.nashtech.assetmanagement.dto.response.AssignmentDto;
import com.nashtech.assetmanagement.dto.response.ListAssignmentResponse;
import com.nashtech.assetmanagement.entities.Asset;
import com.nashtech.assetmanagement.entities.Assignment;
import com.nashtech.assetmanagement.entities.Users;
import com.nashtech.assetmanagement.mapper.AssignmentContent;
import com.nashtech.assetmanagement.mapper.AssignmentMapper;
import com.nashtech.assetmanagement.repositories.AssetRepository;
import com.nashtech.assetmanagement.repositories.AssignmentRepository;
import com.nashtech.assetmanagement.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @DisplayName("Test for search assignments by asset code or asset name or assignee's username")
    @Test
    void givenTextSearch_whenGetAssignmentsBySearching_thenReturnListAssignmentResponse() {
        //given
        ListAssignmentResponse expectedResponse = mock(ListAssignmentResponse.class);
        Pageable pageable = PageRequest.of(0, 1);
        Page<Assignment> assignmentPage = mock(Page.class);
        String textSearch = "LA100001";
        when(assignmentRepository.searchByAssetCodeOrAssetNameOrUsernameAssignee(
                textSearch.replaceAll(" ", "").toLowerCase(), pageable))
                .thenReturn(assignmentPage);
        when(assignmentContent.getAssignmentResponse(assignmentPage)).thenReturn(expectedResponse);

        //when
        ListAssignmentResponse actualResponse = assignmentServiceImpl.getAssignmentsBySearching(0, 1, textSearch);
        //then
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

//    @Test
//    void givenInvalidAssignedDate_whenGetAllAssignmentByStateOrAssignedDate_thenThrowsException() {
//        //when
//        String assignedDateStr = "2030-12-30";
//        Date assignedDate = mock(Date.class);
//        assignedDate = Date.valueOf(assignedDateStr);
//        //when
//        DateInvalidException exception = Assertions.assertThrows(DateInvalidException.class,
//                () -> assignmentServiceImpl.getAllAssignmentByStateOrAssignedDate(0,1, ))
//
//    }

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
        assertThat(result).isEqualTo(response);
    }

}