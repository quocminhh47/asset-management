package com.nashtech.assetmanagement.service.impl;

import com.nashtech.assetmanagement.dto.request.AssignmentRequestDto;
import com.nashtech.assetmanagement.dto.request.ChangeAssignmentStateRequestDto;
import com.nashtech.assetmanagement.dto.request.DeleteAssignmentRequestDto;
import com.nashtech.assetmanagement.dto.request.EditAssignmentRequestDto;
import com.nashtech.assetmanagement.dto.response.AssignmentResponseDto;
import com.nashtech.assetmanagement.dto.response.ListAssignmentResponseDto;
import com.nashtech.assetmanagement.dto.response.MessageResponse;
import com.nashtech.assetmanagement.entities.Asset;
import com.nashtech.assetmanagement.entities.Assignment;
import com.nashtech.assetmanagement.entities.AssignmentId;
import com.nashtech.assetmanagement.entities.Users;
import com.nashtech.assetmanagement.enums.AssetState;
import com.nashtech.assetmanagement.exception.DateInvalidException;
import com.nashtech.assetmanagement.exception.NotUniqueException;
import com.nashtech.assetmanagement.exception.RequestNotAcceptException;
import com.nashtech.assetmanagement.exception.ResourceNotFoundException;
import com.nashtech.assetmanagement.mapper.AssignmentContent;
import com.nashtech.assetmanagement.mapper.AssignmentMapper;
import com.nashtech.assetmanagement.repositories.AssetRepository;
import com.nashtech.assetmanagement.repositories.AssignmentRepository;
import com.nashtech.assetmanagement.repositories.UserRepository;
import com.nashtech.assetmanagement.service.AssignmentService;
import com.nashtech.assetmanagement.service.AuthenticationServices;
import com.nashtech.assetmanagement.utils.AppConstants;
import com.nashtech.assetmanagement.utils.StateConverter;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.nashtech.assetmanagement.utils.AppConstants.DECLINED;
import static com.nashtech.assetmanagement.utils.AppConstants.WAITING_FOR_ACCEPTANCE;

@Service
@AllArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final AssignmentContent assignmentContent;
    private final UserRepository userRepository;
    private final AssetRepository assetRepository;
    private final AssignmentMapper assignmentMapper;
    private final AuthenticationServices authenticationService;

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
        Page<Assignment> assignmentPage;
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
    @Transactional(rollbackFor = {SQLException.class, NotUniqueException.class})
    public AssignmentResponseDto createNewAssignment(AssignmentRequestDto request) {
        if (assignmentRepository.existsById_AssetCodeAndId_AssignedDateAndId_AssignedTo
                (request.getAssetCode(), request.getAssignedDate(), request.getAssignedTo())) {
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
        Assignment assignment = assignmentMapper.mapRequestAssignmentToAssignment(request);
        assignment.setAssignedTo(assignTo.get());
        assignment.setAssignedBy(assignBy.get());
        assignment.setAsset(asset.get());
        assignmentRepository.save(assignment);
        return assignmentMapper.mapAssignmentToResponseDto(assignment);
    }

    @Override
    @Transactional(rollbackFor = {SQLException.class, NotUniqueException.class, ResourceNotFoundException.class})
    public AssignmentResponseDto editAssignment(EditAssignmentRequestDto requestDto) {
        AssignmentId assignmentId = new AssignmentId(
                requestDto.getOldAssignedTo(),
                requestDto.getOldAssetCode(),
                requestDto.getOldAssignedDate());
        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(
                () -> new ResourceNotFoundException("Assignment.not.found"));
        if (!assignment.getState().equals(AppConstants.WAITING_FOR_ACCEPTANCE)) {
            throw new IllegalStateException("Assignment.state.invalid");
        }
        if (requestDto.getOldAssignedDate().compareTo(requestDto.getAssignedDate()) > 0) {
            throw new DateInvalidException("New.assignment.date.occurs.before.old.assignment.date");
        }
        Users assignBy = authenticationService.getUser();
        if (requestDto.getOldAssignedDate().compareTo(requestDto.getAssignedDate()) == 0
                && requestDto.getOldAssignedTo().equals(requestDto.getAssignedToStaffCode())
                && requestDto.getOldAssetCode().equals(requestDto.getAssetCode())) {
            assignment.setNote(requestDto.getNote());
            assignment.setAssignedBy(assignBy);
            return assignmentMapper.mapAssignmentToResponseDto(assignment);
        }
        assignment.getAsset().setState(AssetState.AVAILABLE);
        if (assignmentRepository.existsById_AssetCodeAndId_AssignedDateAndId_AssignedTo
                (requestDto.getAssetCode(), requestDto.getAssignedDate(), requestDto.getAssignedToStaffCode())) {
            throw new NotUniqueException("New.assignment.exist");
        }
        Asset asset = assetRepository.findById(requestDto.getAssetCode()).orElseThrow(
                () -> new ResourceNotFoundException("Asset.not.found"));
        Users assignTo = userRepository.findById(requestDto.getAssignedToStaffCode()).orElseThrow(
                () -> new ResourceNotFoundException("Assign.to.user.not.found"));
        Assignment newAssignment = assignmentMapper.mapEditAssignmentToAssignment(requestDto, asset, assignTo, assignBy);
        assignmentRepository.delete(assignment);
        assignmentRepository.save(newAssignment);
        return assignmentMapper.mapAssignmentToResponseDto(newAssignment);
    }


    @Override
    public List<AssignmentResponseDto> getListAssignmentByAssetCode(String assetCode) {
        Optional<Asset> optionalAsset = assetRepository.findById(assetCode);
        if (optionalAsset.isEmpty()) {
            throw new ResourceNotFoundException(String.format("asset.not.found.with.code:%s", assetCode));
        }
        Asset asset = optionalAsset.get();
        List<Assignment> list = assignmentRepository.findByAsset(asset);
        return assignmentMapper.mapperListAssignment(list);
    }


    @Override
    public List<AssignmentResponseDto> getListAssignmentByUser(String userId, String sortBy, String sortDirection) {
        Sort.Direction sort = Sort.Direction.ASC;
        if (sortDirection.equals("DESC")) {
            sort = Sort.Direction.DESC;
        }
        Pageable pageable = PageRequest.of(0, AppConstants.HOME_SIZE, Sort.by(sort, sortBy));
        Optional<Users> optionalUsers = userRepository.findById(userId);
        if (optionalUsers.isEmpty()) {
            throw new ResourceNotFoundException(String.format("user.not.found.with.code:%s", userId));
        }
        Page<Assignment> pageAssignment = assignmentRepository.getListAssignmentByUser(userId, pageable);
        List<Assignment> listEntity = pageAssignment.getContent();
        return assignmentMapper.mapperListAssignment(listEntity);
    }


    @Override
    public MessageResponse updateAssignmentState(ChangeAssignmentStateRequestDto changeAssignmentStateRequestDto) {
        if (!(changeAssignmentStateRequestDto.getState().equalsIgnoreCase(AppConstants.ACCEPTED))
                && !(changeAssignmentStateRequestDto.getState().equalsIgnoreCase(DECLINED))) {
            return new MessageResponse(HttpStatus.BAD_REQUEST, "Assignment state request is not valid", new java.util.Date());
        }
        Users users = userRepository.findByUserName(changeAssignmentStateRequestDto.getAssignedTo()).orElseThrow(
                () -> new ResourceNotFoundException("Cannot find user with username: " + changeAssignmentStateRequestDto.getAssignedTo()));

        AssignmentId assignmentId = new AssignmentId(
                users.getStaffCode(),
                changeAssignmentStateRequestDto.getAssetCode(),
                changeAssignmentStateRequestDto.getAssignedDate());
        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(
                () -> new ResourceNotFoundException("Cannot find assignment with assignment id: " + assignmentId));
        if (!assignment.getState().equalsIgnoreCase(WAITING_FOR_ACCEPTANCE)) {
            return new MessageResponse(HttpStatus.CONFLICT, "Assignment state in database is Accepted or Declined", new java.util.Date());
        }


        if (changeAssignmentStateRequestDto.getState().equalsIgnoreCase(DECLINED)) {
            Asset asset = assetRepository.findById(changeAssignmentStateRequestDto.getAssetCode()).orElseThrow(
                    () -> new ResourceNotFoundException("Cannot find asset with asset code: " +
                            changeAssignmentStateRequestDto.getAssetCode()));
            asset.setState(AssetState.AVAILABLE);
            assetRepository.save(asset);
        }

        assignment.setState(changeAssignmentStateRequestDto.getState());
        assignmentRepository.save(assignment);
        return new MessageResponse(
                HttpStatus.OK,
                "Update assignment state to " + changeAssignmentStateRequestDto.getState() + " successfully!",
                new java.util.Date());
    }

    @Override
    public MessageResponse deleteAssignment(DeleteAssignmentRequestDto deleteAssignmentRequestDto) {
        Users users = userRepository.findByUserName(deleteAssignmentRequestDto.getAssignedTo()).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Cannot find user with username: " + deleteAssignmentRequestDto.getAssignedTo()));

        AssignmentId assignmentId = new AssignmentId(
                users.getStaffCode(),
                deleteAssignmentRequestDto.getAssetCode(),
                deleteAssignmentRequestDto.getAssignedDate());

        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(
                () -> new ResourceNotFoundException("Cannot find assignment"));
        String assignmentState = assignment.getState();
        if (!(assignmentState.equalsIgnoreCase(WAITING_FOR_ACCEPTANCE) || (assignmentState.equalsIgnoreCase(DECLINED)))) {
            throw new RequestNotAcceptException("can only be deleted when the status is" +
                    " 'Waiting for accept' and 'Declined'. Current state: '" + assignmentState.toLowerCase() + "'.");
        }
        assignmentRepository.delete(assignment);
        Asset asset = assetRepository.findById(deleteAssignmentRequestDto.getAssetCode()).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Cannot find asset with id: " + deleteAssignmentRequestDto.getAssetCode()));

        asset.setState(AssetState.AVAILABLE);

        assetRepository.save(asset);

        return new MessageResponse(
                HttpStatus.OK,
                "Assignment deleted",
                new java.util.Date());
    }
}
