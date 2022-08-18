package com.nashtech.assetmanagement.mapper;


import com.nashtech.assetmanagement.dto.request.AssignmentRequestDto;
import com.nashtech.assetmanagement.dto.request.EditAssignmentRequestDto;
import com.nashtech.assetmanagement.dto.response.AssignmentResponseDto;
import com.nashtech.assetmanagement.entities.Asset;
import com.nashtech.assetmanagement.entities.Assignment;
import com.nashtech.assetmanagement.entities.AssignmentId;
import com.nashtech.assetmanagement.entities.Users;
import com.nashtech.assetmanagement.enums.AssetState;
import com.nashtech.assetmanagement.utils.AppConstants;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class AssignmentMapper {
    private final ModelMapper mapper;

    public Assignment mapRequestAssignmentToAssignment(AssignmentRequestDto request) {
        AssignmentId assignmentId = new AssignmentId(request.getAssignedTo(), request.getAssetCode(), request.getAssignedDate());
        Assignment assignment = new Assignment();
        assignment.setId(assignmentId);
        assignment.setNote(request.getNote());
        assignment.setState("Waiting for acceptance");
        return assignment;
    }
    public Assignment mapEditAssignmentToAssignment(EditAssignmentRequestDto requestDto, Asset asset, Users assignTo, Users assignBy){
        AssignmentId assignmentId = new AssignmentId(requestDto.getAssignedToStaffCode(), requestDto.getAssetCode(), requestDto.getAssignedDate());
        Assignment assignment = new Assignment();
        assignment.setId(assignmentId);
        assignment.setNote(requestDto.getNote());
        assignment.setState(AppConstants.WAITING_FOR_ACCEPTANCE);
        assignment.setAssignedBy(assignBy);
        assignment.setAssignedTo(assignTo);
        assignment.setAsset(asset);
        asset.setState(AssetState.ASSIGNED);
        return assignment;
    }

    public AssignmentResponseDto mapAssignmentToResponseDto(Assignment assignment) {
        return mapper.map(assignment,AssignmentResponseDto.class);
    }

    public List<AssignmentResponseDto> mapperListAssignment(List<Assignment> list) {
        return list
                .stream()
                .map(item -> mapper.map(item, AssignmentResponseDto.class))
                .collect(Collectors.toList());
    }
}
