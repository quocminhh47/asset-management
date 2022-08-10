package com.nashtech.assetmanagement.service.impl;

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
	public ListRequestReturningResponseDto getListRequestReturning(List<String> states, Date returnedDate,
			String keyword, String sortBy, String sortDirection, Integer page, Integer size) {
		Sort.Direction sort = Sort.Direction.ASC;
		if (sortDirection.equals("DESC")) {
			sort = Sort.Direction.DESC;
		}

		Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sort, sortBy));

		List<RequestReturningState> requestReturningState = new ArrayList<>();
		
		for (int i = 0; i < states.size(); i++) {
			requestReturningState.add(RequestReturningState.valueOf(states.get(i)));
		}
		System.out.println(requestReturningState);
		Page<RequestReturning> pageRequestReturning = requestReturningRepository
				.getListRequestReturning(requestReturningState, returnedDate, keyword, pageable);
		
		List<RequestReturning> dtoEntity = pageRequestReturning.getContent();

		List<RequestReturningResponseDto> listDto = requestReturningMapper.mapperListRequestReturning(dtoEntity);
		System.out.println(listDto.size());
		long totalItems = pageRequestReturning.getTotalPages();
		System.out.println(totalItems);
		
		ListRequestReturningResponseDto result = new ListRequestReturningResponseDto(listDto, totalItems);
		return result;
	}

}
