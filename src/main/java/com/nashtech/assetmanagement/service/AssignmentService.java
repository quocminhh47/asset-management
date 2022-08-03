package com.nashtech.assetmanagement.service;

import java.util.List;

import com.nashtech.assetmanagement.dto.request.RequestAssignmentDTO;
import com.nashtech.assetmanagement.dto.response.AssignmentDto;
import com.nashtech.assetmanagement.dto.response.ListAssignmentResponse;

public interface AssignmentService {


    AssignmentDto createNewAssignment(RequestAssignmentDTO request);
    
    List<AssignmentDto> getListAssignmentByAssetCode(String assetCode);

    ListAssignmentResponse getAssignmentsByCondition(int pageNo,
                                                     int pageSize,
                                                     String text,
                                                     List<String> states,
                                                     String assignedDateStr);
    
    List<AssignmentDto> getListAssignmentByUser(String userId, String sortBy, String sortDirection);
}
