package com.nashtech.assetmanagement.service.impl;

import com.nashtech.assetmanagement.dto.response.ListAssignmentResponse;
import com.nashtech.assetmanagement.entities.Assignment;
import com.nashtech.assetmanagement.exception.DateInvalidException;
import com.nashtech.assetmanagement.mapper.AssignmentContent;
import com.nashtech.assetmanagement.repositories.AssignmentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.sql.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AssignmentServiceImplTest {

    AssignmentRepository assignmentRepository;
    AssignmentContent assignmentContent;
    AssignmentServiceImpl assignmentServiceImpl;

    @BeforeEach
    void setUp() {
        assignmentRepository = mock(AssignmentRepository.class);
        assignmentContent = mock(AssignmentContent.class);
        assignmentServiceImpl = new AssignmentServiceImpl(assignmentRepository, assignmentContent);

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
        ListAssignmentResponse actualResponse = assignmentServiceImpl.getAssignmentsBySearching(0,1, textSearch);
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
}