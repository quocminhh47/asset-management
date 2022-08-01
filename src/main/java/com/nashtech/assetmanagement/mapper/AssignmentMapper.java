package com.nashtech.assetmanagement.mapper;


import com.nashtech.assetmanagement.dto.request.RequestAssignmentDTO;
import com.nashtech.assetmanagement.dto.response.AssignmentDto;
import com.nashtech.assetmanagement.entities.Assignment;
import com.nashtech.assetmanagement.entities.AssignmentId;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AssignmentMapper {
    private final ModelMapper mapper;

    public Assignment MapRequestAssignmentToAssignment(RequestAssignmentDTO request) {
        AssignmentId assignmentId = new AssignmentId(request.getAssignedTo(), request.getAssetCode(), request.getAssignedDate());
        Assignment assignment = new Assignment();
        assignment.setId(assignmentId);
        assignment.setNote(request.getNote());
        assignment.setState("Waiting for acceptance");
        return assignment;
    }

    public AssignmentDto MapAssignmentToResponseDto(Assignment assignment) {
        AssignmentDto responseDTO = new AssignmentDto();
        responseDTO.setAssetCode(assignment.getAsset().getCode());
        responseDTO.setAssetName(assignment.getAsset().getName());
        responseDTO.setAssetSpecification(assignment.getAsset().getSpecification());
        responseDTO.setAssignedToUsername(assignment.getAssignedTo().getUserName());
        responseDTO.setAssignedByUsername(assignment.getAssignedBy().getUserName());
        responseDTO.setIdAssignedDate(assignment.getId().getAssignedDate());
        responseDTO.setNote(assignment.getNote());
        responseDTO.setState(assignment.getState());

        return responseDTO;
    }
}
