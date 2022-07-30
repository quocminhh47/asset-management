package com.nashtech.assetmanagement.mapper;


import com.nashtech.assetmanagement.dto.request.RequestAssignmentDTO;
import com.nashtech.assetmanagement.dto.response.AssignmentResponseDTO;
import com.nashtech.assetmanagement.entities.Assignment;
import com.nashtech.assetmanagement.entities.AssignmentId;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AssignmentMapper {
    private final ModelMapper mapper;

    public AssignmentMapper(ModelMapper mapper) {
        this.mapper=mapper;
    }

    public Assignment MapRequestAssignmentToAssignment(RequestAssignmentDTO request){
        AssignmentId assignmentId = new AssignmentId(request.getAssignedTo(), request.getAssetCode(), request.getAssignedDate());
        Assignment assignment = new Assignment();
        assignment.setId(assignmentId);
        assignment.setNote(request.getNote());
        assignment.setState("Waiting for acceptance");
        return assignment;
    }

    public AssignmentResponseDTO MapAssignmentToResponseDto(Assignment assignment) {
        AssignmentResponseDTO responseDTO = new AssignmentResponseDTO();
        responseDTO.setAssignedBy(assignment.getAssignedBy().getStaffCode());
        responseDTO.setAssignedTo(assignment.getAssignedBy().getStaffCode());
        responseDTO.setAssignedDate(assignment.getId().getAssignedDate());
        responseDTO.setNote(assignment.getNote());
        responseDTO.setState(assignment.getState());
        responseDTO.setAssetCode(assignment.getAsset().getCode());
        return responseDTO;
    }
}
