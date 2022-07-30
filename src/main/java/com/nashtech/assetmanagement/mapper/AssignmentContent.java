package com.nashtech.assetmanagement.mapper;

import com.nashtech.assetmanagement.dto.response.AssignmentDto;
import com.nashtech.assetmanagement.dto.response.ListAssignmentResponse;
import com.nashtech.assetmanagement.entities.Assignment;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class AssignmentContent {

    private final ModelMapper mapper;

    public AssignmentDto mapToAssignmentDto(Assignment assignment) {
        return mapper.map(assignment, AssignmentDto.class);
    }

    public ListAssignmentResponse getAssignmentResponse(Page<Assignment> assignmentPage) {
        List<Assignment> assignments = assignmentPage.getContent();
        List<AssignmentDto> assignmentContent = assignments.stream()
                .map(this::mapToAssignmentDto)
                .collect(Collectors.toList());
        return ListAssignmentResponse.builder()
                .assignmentContent(assignmentContent)
                .pageNo(assignmentPage.getNumber())
                .pageSize(assignmentPage.getSize())
                .totalElements(assignmentPage.getTotalElements())
                .totalPages(assignmentPage.getTotalPages())
                .last(assignmentPage.isLast())
                .build();
    }
}
