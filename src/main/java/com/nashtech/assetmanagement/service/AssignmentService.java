package com.nashtech.assetmanagement.service;

import com.nashtech.assetmanagement.dto.request.RequestAssignmentDTO;
import com.nashtech.assetmanagement.dto.response.AssignmentResponseDTO;

public interface AssignmentService {

    AssignmentResponseDTO createNewAssignment(RequestAssignmentDTO request);
}
