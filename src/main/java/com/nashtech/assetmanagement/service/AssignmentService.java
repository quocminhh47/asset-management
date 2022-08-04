package com.nashtech.assetmanagement.service;

import java.util.List;

import com.nashtech.assetmanagement.dto.request.AssignmentRequestDto;
import com.nashtech.assetmanagement.dto.response.AssignmentResponseDto;
import com.nashtech.assetmanagement.dto.response.ListAssignmentResponseDto;

public interface AssignmentService {


    AssignmentResponseDto createNewAssignment(AssignmentRequestDto request);
    
    List<AssignmentResponseDto> getListAssignmentByAssetCode(String assetCode);

    ListAssignmentResponseDto getAssignmentsByCondition(int pageNo,
                                                     int pageSize,
                                                     String text,
                                                     List<String> states,
                                                     String assignedDateStr);
    
    List<AssignmentResponseDto> getListAssignmentByUser(String userId, String sortBy, String sortDirection);
}
