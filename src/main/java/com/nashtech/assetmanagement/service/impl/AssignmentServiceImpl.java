package com.nashtech.assetmanagement.service.impl;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.nashtech.assetmanagement.exception.DateInvalidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nashtech.assetmanagement.dto.request.AssignmentRequestDto;
import com.nashtech.assetmanagement.dto.response.AssignmentResponseDto;
import com.nashtech.assetmanagement.dto.response.ListAssignmentResponseDto;
import com.nashtech.assetmanagement.entities.Asset;
import com.nashtech.assetmanagement.entities.Assignment;
import com.nashtech.assetmanagement.entities.Users;
import com.nashtech.assetmanagement.enums.AssetState;
import com.nashtech.assetmanagement.exception.NotUniqueException;
import com.nashtech.assetmanagement.exception.ResourceNotFoundException;
import com.nashtech.assetmanagement.mapper.AssignmentContent;
import com.nashtech.assetmanagement.mapper.AssignmentMapper;
import com.nashtech.assetmanagement.repositories.AssetRepository;
import com.nashtech.assetmanagement.repositories.AssignmentRepository;
import com.nashtech.assetmanagement.repositories.UserRepository;
import com.nashtech.assetmanagement.service.AssignmentService;
import com.nashtech.assetmanagement.utils.StateConverter;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import java.sql.SQLException;

import static java.rmi.server.LogStream.log;

@Service
@AllArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final AssignmentContent assignmentContent;
    private final UserRepository userRepository;
    private final AssetRepository assetRepository;
    private final AssignmentMapper assignmentMapper;

    @Override
    public ListAssignmentResponseDto getAssignmentsByCondition(int pageNo,
                                                            int pageSize,
                                                            String text,
                                                            List<String> states,
                                                            String assignedDateStr) {
        List<String> assignmentState = states.stream()
                .map(StateConverter::getAssignmentState)
                .collect(Collectors.toList());

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Assignment> assignmentPage = null;
        Date assignedDate;

        if (!assignedDateStr.replaceAll(" ", "").isBlank()) {
            try {
                assignedDate = Date.valueOf(assignedDateStr); //pattern: yyyy/mm/dd
                assignmentPage = assignmentRepository.getAssignmentByConditions(
                        text.toLowerCase(),
                        assignmentState,
                        assignedDate,
                        pageable);
            } catch (IllegalArgumentException e) {
                throw new DateInvalidException("Date.format.is.not.valid", e);
            }
        } else {
            assignmentPage = assignmentRepository.getAssignmentWithoutAssignedDate(
                    text.toLowerCase(),
                    assignmentState,
                    pageable);
        }
        return assignmentContent.getAssignmentResponse(assignmentPage);
    }


    @Override
    @Transactional(rollbackFor = {SQLException.class,NotUniqueException.class})
    public AssignmentResponseDto createNewAssignment(AssignmentRequestDto request) {
        if(assignmentRepository.existsById_AssetCodeAndId_AssignedDateAndId_AssignedTo
                (request.getAssetCode(),request.getAssignedDate(),request.getAssignedTo())){
            throw new NotUniqueException("AssignmentId.is.exist");
        }
        Optional<Asset> asset = assetRepository.findById(request.getAssetCode());
        if (asset.isEmpty()) {
            throw new ResourceNotFoundException("AssetCode not found");
        }
        Optional<Users> assignTo = userRepository.findById(request.getAssignedTo());
        if (assignTo.isEmpty()) {
            throw new ResourceNotFoundException("Assign to User not found");
        }
        Optional<Users> assignBy = userRepository.findById(request.getAssignedBy());
        if (assignBy.isEmpty()) {
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

    @Override
    public List<AssignmentResponseDto> getListAssignmentByAssetCode(String assetCode) {
        Optional<Asset> optionalAsset = assetRepository.findById(assetCode);
        if (!optionalAsset.isPresent()) {
            throw new ResourceNotFoundException(String.format("asset.not.found.with.code:%s", assetCode));
        }
        Asset asset = optionalAsset.get();
        List<Assignment> list = assignmentRepository.findByAsset(asset);
        List<AssignmentResponseDto> resultList = assignmentMapper.mapperListAssignment(list);
        return resultList;

    }


	@Override
	public List<AssignmentResponseDto> getListAssignmentByUser(String userId, String sortBy, String sortDirection) {
		Sort.Direction sort = Sort.Direction.ASC;
		if(sortDirection.equals("DESC")) {
			sort = Sort.Direction.DESC;
		}
		Pageable pageable = PageRequest.of(0, 20 , Sort.by(sort, sortBy));
		Optional<Users> optionalUsers = userRepository.findById(userId);
		if (!optionalUsers.isPresent()) {
			throw new ResourceNotFoundException(String.format("user.not.found.with.code:%s", userId));
		}
		Page<Assignment> pageAssignment = assignmentRepository.getListAssignmentByUser(userId, pageable);
		List<Assignment> listEntity = pageAssignment.getContent();
		List<AssignmentResponseDto> listResponse = assignmentMapper.mapperListAssignment(listEntity);
		return listResponse;
	}
}
