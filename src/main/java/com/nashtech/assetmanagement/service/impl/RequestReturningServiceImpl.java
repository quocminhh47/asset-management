package com.nashtech.assetmanagement.service.impl;


import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import com.nashtech.assetmanagement.dto.request.ReturningRequestDto;
import com.nashtech.assetmanagement.entities.*;
import com.nashtech.assetmanagement.enums.AssetState;
import com.nashtech.assetmanagement.exception.BadRequestException;
import com.nashtech.assetmanagement.exception.ResourceNotFoundException;
import com.nashtech.assetmanagement.repositories.AssetRepository;
import com.nashtech.assetmanagement.repositories.AssignmentRepository;
import com.nashtech.assetmanagement.service.AuthenticationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import com.nashtech.assetmanagement.dto.request.CreateRequestReturningAssetRequestDto;
import com.nashtech.assetmanagement.dto.response.CreateRequestReturningResponseDto;
import com.nashtech.assetmanagement.repositories.UserRepository;
import com.nashtech.assetmanagement.utils.AppConstants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nashtech.assetmanagement.dto.request.RequestReturningRequestGetListDto;
import com.nashtech.assetmanagement.dto.response.ListRequestReturningResponseDto;
import com.nashtech.assetmanagement.dto.response.RequestReturningResponseDto;
import com.nashtech.assetmanagement.enums.RequestReturningState;
import com.nashtech.assetmanagement.exception.DateInvalidException;
import com.nashtech.assetmanagement.mapper.RequestReturningMapper;
import com.nashtech.assetmanagement.repositories.RequestReturningRepository;
import com.nashtech.assetmanagement.service.RequestReturningService;
import static com.nashtech.assetmanagement.utils.AppConstants.DONE;
import static com.nashtech.assetmanagement.enums.RequestReturningState.WAITING_FOR_RETURNING;

@Service
@AllArgsConstructor
@Slf4j
public class RequestReturningServiceImpl implements RequestReturningService {

	private final UserRepository userRepository;

	private final AssetRepository assetRepository;

	private final AssignmentRepository assignmentRepository;

	private final ModelMapper mapper;

	private final RequestReturningRepository requestReturningRepository;

	private final RequestReturningMapper requestReturningMapper;

	private final AuthenticationService authenticationService;

	 static final String INVALID_STATE = "The returning request is invalid. You can only create returning" +
			"request only when its state is : 'Waiting for returning'! ";

	@Override
	public ListRequestReturningResponseDto getListRequestReturning(RequestReturningRequestGetListDto dto) {

		long totalItems = 0;
		List<RequestReturning> listEntity = new ArrayList<>();

		Sort.Direction sort = Sort.Direction.ASC;
		if (dto.getSortDirection().equals("DESC")) {
			sort = Sort.Direction.DESC;
		}
		
		Pageable pageable = PageRequest.of(dto.getPage() - 1, dto.getSize(), Sort.by(sort, dto.getSortBy()));
		List<RequestReturningState> requestReturningState = requestReturningMapper.mapperListStates(dto.getStates());

		if (dto.getReturnedDate().equals("")) {
			Page<RequestReturning> pageRequestReturning = requestReturningRepository
					.getListRequestReturningByStates(requestReturningState, dto.getKeyword(), pageable);
			totalItems = pageRequestReturning.getTotalPages();
			listEntity = pageRequestReturning.getContent();
		} else {
			try {
				Date date = Date.valueOf(dto.getReturnedDate());
				Page<RequestReturning> pageRequestReturning = requestReturningRepository
						.getListRequestReturning(requestReturningState, date, dto.getKeyword(), pageable);
				totalItems = pageRequestReturning.getTotalPages();
				listEntity = pageRequestReturning.getContent();
			} catch (Exception e) {
				throw new DateInvalidException("Date.format.is.not.valid");
			}
		}
		
		List<RequestReturningResponseDto> listDto = requestReturningMapper.mapperListRequestReturning(listEntity);
		ListRequestReturningResponseDto result = new ListRequestReturningResponseDto(listDto, totalItems);
		return result;
	}


	@Override
	public RequestReturningResponseDto completeReturningRequest(ReturningRequestDto requestDto) {
		Date assignedDate;
		try {
			assignedDate = Date.valueOf(requestDto.getAssignedDate());
		} catch (Exception e) {
			log.error("Date.format.is.not.valid");
			throw new BadRequestException("Assigned date format is not valid !");
		}

		AssignmentId assignmentId = new AssignmentId(requestDto.getAssignedTo(), requestDto.getAssetCode(), assignedDate);
		Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(
				() -> new ResourceNotFoundException("Assignment is not exist !"));
		RequestReturning returningRequest = assignment.getRequestReturning();

		RequestReturningState requestReturningState = returningRequest.getState();
		if(requestReturningState.equals(RequestReturningState.COMPLETED)) {
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


	//587 - Create request for returning asset
	@Override
	public CreateRequestReturningResponseDto createRequestReturningAsset(CreateRequestReturningAssetRequestDto createRequestReturningAssetRequestDto) {
		// Validate exist data & valid data
		Users requestedByUser = userRepository.findByUserName(createRequestReturningAssetRequestDto.getRequestedBy()).orElseThrow(
				() -> new ResourceNotFoundException("Cannot find requestedByUser with username: " + createRequestReturningAssetRequestDto.getRequestedBy()));

		assetRepository.findById(createRequestReturningAssetRequestDto.getAssetCode()).orElseThrow(
				() -> new ResourceNotFoundException("Cannot find asset with asset code: " + createRequestReturningAssetRequestDto.getAssetCode()));

		AssignmentId assignmentId = new AssignmentId(
				requestedByUser.getStaffCode(),
				createRequestReturningAssetRequestDto.getAssetCode(),
				createRequestReturningAssetRequestDto.getAssignedDate());

		Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(
				() -> new ResourceNotFoundException("Cannot find assignment with assignment id: " + assignmentId));

		if(!assignment.getState().equalsIgnoreCase(AppConstants.ACCEPTED)) {
			throw new RuntimeException("Request returning is only enabled for assignments have state is Accepted");
		}

		Optional<RequestReturning> requestReturningOptional = requestReturningRepository.getRequestReturningByAssignment(assignment);
		if(!requestReturningOptional.isEmpty()) {
			throw new RuntimeException("One assignment must have only one request returning");
		}

		// Create request returning
		RequestReturning requestReturning = RequestReturning.builder()
				.requestedBy(requestedByUser)
				.assignment(assignment)
				.returnedDate(createRequestReturningAssetRequestDto.getReturnedDate())
				.state(WAITING_FOR_RETURNING)
				.build();

		requestReturningRepository.save(requestReturning);

		// Map Dto and return
		return mapper.map(requestReturning, CreateRequestReturningResponseDto.class);
	}

}
