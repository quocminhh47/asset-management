package com.nashtech.assetmanagement.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nashtech.assetmanagement.dto.response.ListRequestReturningResponseDto;
import com.nashtech.assetmanagement.dto.response.RequestReturningResponseDto;
import com.nashtech.assetmanagement.entities.RequestReturning;
import com.nashtech.assetmanagement.enums.RequestReturningState;
import com.nashtech.assetmanagement.exception.DateInvalidException;
import com.nashtech.assetmanagement.mapper.RequestReturningMapper;
import com.nashtech.assetmanagement.repositories.RequestReturningRepository;
import com.nashtech.assetmanagement.service.RequestReturningService;

@Service
public class RequestReturningServiceImpl implements RequestReturningService {

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

}
