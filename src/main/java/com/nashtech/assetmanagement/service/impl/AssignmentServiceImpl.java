package com.nashtech.assetmanagement.service.impl;

import com.nashtech.assetmanagement.dto.request.RequestAssignmentDTO;
import com.nashtech.assetmanagement.dto.response.AssignmentResponseDTO;
import com.nashtech.assetmanagement.entities.Asset;
import com.nashtech.assetmanagement.entities.Assignment;
import com.nashtech.assetmanagement.entities.Users;
import com.nashtech.assetmanagement.enums.AssetState;
import com.nashtech.assetmanagement.exception.ResourceNotFoundException;
import com.nashtech.assetmanagement.mapper.AssignmentMapper;
import com.nashtech.assetmanagement.repositories.AssetRepository;
import com.nashtech.assetmanagement.repositories.AssignmentRepository;
import com.nashtech.assetmanagement.repositories.UserRepository;
import com.nashtech.assetmanagement.service.AssignmentService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AssignmentServiceImpl implements AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;
    private final AssetRepository assetRepository;
    private final AssignmentMapper assignmentMapper;

    public AssignmentServiceImpl(AssignmentRepository assignmentRepository, UserRepository userRepository
            , AssetRepository assetRepository, AssignmentMapper assignmentMapper) {
        this.assignmentRepository = assignmentRepository;
        this.userRepository = userRepository;
        this.assetRepository = assetRepository;
        this.assignmentMapper = assignmentMapper;
    }

    @Override
    public AssignmentResponseDTO createNewAssignment(RequestAssignmentDTO request) {
        Optional<Asset> asset = assetRepository.findById(request.getAssetCode());
        if(asset.isEmpty()){
            throw new ResourceNotFoundException("AssetCode not found");
        }
        Optional<Users> assignTo = userRepository.findById(request.getAssignedTo());
        if(assignTo.isEmpty()){
            throw new ResourceNotFoundException("Assign to User not found");
        }
        Optional<Users> assignBy = userRepository.findById(request.getAssignedBy());
        if (assignBy.isEmpty()){
            throw new ResourceNotFoundException("Assign by User not found");
        }
        asset.get().setState(AssetState.ASSIGNED);
        Assignment assignment = assignmentMapper.MapRequestAssignmentToAssignment(request);
        assignment.setAssignedTo(assignTo.get());
        assignment.setAssignedBy(assignBy.get());
        assignment.setAsset(asset.get());
        assignmentRepository.save(assignment);
        return assignmentMapper.MapAssignmentToResponseDto(assignment);
    }
}
