package com.nashtech.assetmanagement.service;

import com.nashtech.assetmanagement.dto.request.RequestAssignmentDTO;
import com.nashtech.assetmanagement.dto.response.ResponseAssignmentDto;

public interface AssignmentService {

    ResponseAssignmentDto createNewAssignment(RequestAssignmentDTO request);
}
