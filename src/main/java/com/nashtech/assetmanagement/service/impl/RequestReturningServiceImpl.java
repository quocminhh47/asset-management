package com.nashtech.assetmanagement.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.nashtech.assetmanagement.dto.request.CreateRequestReturningAssetRequestDto;
import com.nashtech.assetmanagement.dto.response.CreateRequestReturningResponseDto;
import com.nashtech.assetmanagement.entities.*;
import com.nashtech.assetmanagement.exception.ResourceNotFoundException;
import com.nashtech.assetmanagement.repositories.AssetRepository;
import com.nashtech.assetmanagement.repositories.AssignmentRepository;
import com.nashtech.assetmanagement.repositories.UserRepository;
import com.nashtech.assetmanagement.utils.AppConstants;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nashtech.assetmanagement.dto.response.ListRequestReturningResponseDto;
import com.nashtech.assetmanagement.dto.response.RequestReturningResponseDto;
import com.nashtech.assetmanagement.enums.RequestReturningState;
import com.nashtech.assetmanagement.exception.DateInvalidException;
import com.nashtech.assetmanagement.mapper.RequestReturningMapper;
import com.nashtech.assetmanagement.repositories.RequestReturningRepository;
import com.nashtech.assetmanagement.service.RequestReturningService;

import static com.nashtech.assetmanagement.enums.RequestReturningState.WAITING_FOR_RETURNING;

@Service
@AllArgsConstructor
public class RequestReturningServiceImpl implements RequestReturningService {
	private final UserRepository userRepository;
	private final AssetRepository assetRepository;
	private final AssignmentRepository assignmentRepository;
	private final ModelMapper modelMapper;
	@Autowired
	private RequestReturningRepository requestReturningRepository;

	@Autowired
	private RequestReturningMapper requestReturningMapper;

	@Override
	public ListRequestReturningResponseDto getListRequestReturning(List<String> states, String returnedDate,
			String keyword, String sortBy, String sortDirection, Integer page, Integer size){

		Sort.Direction sort = Sort.Direction.ASC;
		if (sortDirection.equals("DESC")) {
			sort = Sort.Direction.DESC;
		}

		Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sort, sortBy));

		List<RequestReturningState> requestReturningState = new ArrayList<>();

		for (int i = 0; i < states.size(); i++) {
			requestReturningState.add(RequestReturningState.valueOf(states.get(i)));
		}
		long totalItems = 0;
		Page<RequestReturning> pageRequestReturning = null;
		if (returnedDate.equals("01/01/1000")) {
			pageRequestReturning = requestReturningRepository.getListRequestReturningByStates(requestReturningState,
					keyword, pageable);
			totalItems = pageRequestReturning.getTotalPages();
		} else {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date date;
			try {
				date = formatter.parse(returnedDate);
			} catch (Exception e) {
				throw new DateInvalidException("Date.format.is.not.valid", e);
			}
			pageRequestReturning = requestReturningRepository.getListRequestReturning(requestReturningState, date,
					keyword, pageable);
			totalItems = pageRequestReturning.getTotalPages();
		}
		List<RequestReturning> dtoEntity = pageRequestReturning.getContent();

		List<RequestReturningResponseDto> listDto = requestReturningMapper.mapperListRequestReturning(dtoEntity);

		ListRequestReturningResponseDto result = new ListRequestReturningResponseDto(listDto, totalItems);
		return result;
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
		return modelMapper.map(requestReturning, CreateRequestReturningResponseDto.class);
	}
}
