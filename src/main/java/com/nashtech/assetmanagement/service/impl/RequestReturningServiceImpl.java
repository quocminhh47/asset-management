package com.nashtech.assetmanagement.service.impl;

import com.nashtech.assetmanagement.dto.request.CreateRequestReturningAssetRequestDto;
import com.nashtech.assetmanagement.dto.request.GetRequestReturningListRequestDto;
import com.nashtech.assetmanagement.dto.request.ReturningRequestDto;
import com.nashtech.assetmanagement.dto.response.*;
import com.nashtech.assetmanagement.entities.*;
import com.nashtech.assetmanagement.enums.AssetState;
import com.nashtech.assetmanagement.enums.RequestReturningState;
import com.nashtech.assetmanagement.exception.BadRequestException;
import com.nashtech.assetmanagement.exception.RequestNotAcceptException;
import com.nashtech.assetmanagement.exception.ResourceNotFoundException;
import com.nashtech.assetmanagement.mapper.RequestReturningMapper;
import com.nashtech.assetmanagement.repositories.AssetRepository;
import com.nashtech.assetmanagement.repositories.AssignmentRepository;
import com.nashtech.assetmanagement.repositories.RequestReturningRepository;
import com.nashtech.assetmanagement.repositories.UserRepository;
import com.nashtech.assetmanagement.service.AuthenticationServices;
import com.nashtech.assetmanagement.service.RequestReturningService;
import com.nashtech.assetmanagement.utils.AppConstants;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static com.nashtech.assetmanagement.enums.RequestReturningState.WAITING_FOR_RETURNING;
import static com.nashtech.assetmanagement.utils.AppConstants.DONE;

@Service
@AllArgsConstructor
public class RequestReturningServiceImpl implements RequestReturningService {

    private final UserRepository userRepository;

    private final AssetRepository assetRepository;

    private final AssignmentRepository assignmentRepository;

    private final ModelMapper mapper;

    private final RequestReturningRepository requestReturningRepository;

    private final RequestReturningMapper requestReturningMapper;

    private final AuthenticationServices authenticationService;

    static final String INVALID_STATE = "The returning request is invalid. You can only create returning" +
            "request only when its state is : 'Waiting for returning'! ";

    @Override
    public ListRequestReturningResponseDto getListRequestReturning(GetRequestReturningListRequestDto dto) {

        long totalItems = 0;
        List<RequestReturning> listEntity = new ArrayList<>();
        Sort.Direction sort = Sort.Direction.ASC;
        if (dto.getSortDirection().equals("DESC")) {
            sort = Sort.Direction.DESC;
        }
        List<RequestReturningState> requestReturningState = requestReturningMapper.mapperListStates(dto.getStates());
        if (dto.getReturnedDate().equals("")) {
            dto.setReturnedDate(null);
        }
        if (dto.getSortBy().equals("acceptedBy.userName")) {
            Pageable pageable = PageRequest.of(dto.getPage() - 1, dto.getSize());
            if (dto.getSortDirection().equals("DESC")) {
                Page<RequestReturning> pageRequestReturning = requestReturningRepository.getListSortByAcceptedByDESC(
                        requestReturningState, dto.getReturnedDate(), dto.getKeyword(), pageable);
                totalItems = pageRequestReturning.getTotalPages();
                listEntity = pageRequestReturning.getContent();
            }
            if (dto.getSortDirection().equals("ASC")) {
                Page<RequestReturning> pageRequestReturning = requestReturningRepository.getListSortByAcceptedByASC(
                        requestReturningState, dto.getReturnedDate(), dto.getKeyword(), pageable);
                totalItems = pageRequestReturning.getTotalPages();
                listEntity = pageRequestReturning.getContent();
            }
        } else {
            Pageable pageable = PageRequest.of(dto.getPage() - 1, dto.getSize(), Sort.by(sort, dto.getSortBy()));
            Page<RequestReturning> pageRequestReturning = requestReturningRepository
                    .getListRequestReturning(requestReturningState, dto.getReturnedDate(), dto.getKeyword(), pageable);
            totalItems = pageRequestReturning.getTotalPages();
            listEntity = pageRequestReturning.getContent();
        }
        List<RequestReturningResponseDto> listDto = requestReturningMapper.mapperListRequestReturning(listEntity);
        return new ListRequestReturningResponseDto(listDto, totalItems);
    }

    @Override
    public RequestReturningResponseDto completeReturningRequest(ReturningRequestDto requestDto) {
        Date assignedDate;
        try {
            assignedDate = Date.valueOf(requestDto.getAssignedDate());
        } catch (Exception e) {
            throw new BadRequestException("Assigned date format is not valid !");
        }

        AssignmentId assignmentId = new AssignmentId(requestDto.getAssignedTo(), requestDto.getAssetCode(), assignedDate);
        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(
                () -> new ResourceNotFoundException("Assignment is not exist !"));
        RequestReturning returningRequest = assignment.getRequestReturning();

        RequestReturningState requestReturningState = returningRequest.getState();
        if (requestReturningState.equals(RequestReturningState.COMPLETED)) {
            throw new BadRequestException(INVALID_STATE);
        }

        Asset asset = assignment.getAsset();


        assignment.setState(DONE);
        returningRequest.setState(RequestReturningState.COMPLETED);
        returningRequest.setAssignment(assignment);

        java.sql.Date returnDate = java.sql.Date.valueOf(LocalDate.now());
        returningRequest.setReturnedDate(returnDate);

        Users acceptedUser = authenticationService.getUser();
        returningRequest.setAcceptedBy(acceptedUser);

        RequestReturning savedRequest = requestReturningRepository.save(returningRequest);

        asset.setState(AssetState.AVAILABLE);
        assetRepository.save(asset);

        return mapper.map(savedRequest, RequestReturningResponseDto.class);
    }

    @Override
    public ListStateRequestReturningResponseDto getRequestReturningState() {
        Map<String, String> listStates = RequestReturningState.getRequestReturningState();
        return new ListStateRequestReturningResponseDto(listStates);
    }


    @Override
    public CreateRequestReturningResponseDto createRequestReturningAsset(CreateRequestReturningAssetRequestDto createRequestReturningAssetRequestDto) {
        // Validate exist data & valid data
        Users requestedByUser = authenticationService.getUser();

        Users assignedToUser =
                userRepository.findByUserName(createRequestReturningAssetRequestDto.getAssignedTo()).orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Cannot find assignedToUser with username: " + createRequestReturningAssetRequestDto.getAssignedTo()));

        assetRepository.findById(createRequestReturningAssetRequestDto.getAssetCode()).orElseThrow(
                () -> new ResourceNotFoundException("Cannot find asset with asset code: " + createRequestReturningAssetRequestDto.getAssetCode()));

        AssignmentId assignmentId = new AssignmentId(
                assignedToUser.getStaffCode(),
                createRequestReturningAssetRequestDto.getAssetCode(),
                createRequestReturningAssetRequestDto.getAssignedDate());

        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(
                () -> new ResourceNotFoundException("Cannot find assignment with assignment id: " + assignmentId));

        if (!assignment.getState().equalsIgnoreCase(AppConstants.ACCEPTED)) {
            throw new RequestNotAcceptException("Request returning is only enabled for assignments have state is Accepted");
        }

        Optional<RequestReturning> requestReturningOptional = requestReturningRepository.getRequestReturningByAssignment(assignment);
        if (requestReturningOptional.isPresent()) {
            throw new RequestNotAcceptException("One assignment must have only one request returning");
        }

        // Create request returning
        RequestReturning requestReturning = RequestReturning.builder()
                .requestedBy(requestedByUser)
                .assignment(assignment)
                .state(WAITING_FOR_RETURNING)
                .build();

        requestReturningRepository.save(requestReturning);

        // Map Dto and return
        return mapper.map(requestReturning, CreateRequestReturningResponseDto.class);
    }

    @Override
    public MessageResponse cancelRequestReturningAssignment(Long id) {
        RequestReturning requestReturning = requestReturningRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Cannot find request returning with id: " + id));
        if (requestReturning.getState() != WAITING_FOR_RETURNING) {
            throw new RequestNotAcceptException("Only request with state 'Waiting for " +
                    "returning' can be deleted");
        }
        requestReturningRepository.delete(requestReturning);
        return new MessageResponse(
                HttpStatus.OK,
                "Cancel successfully!",
                new java.util.Date());
    }
}
