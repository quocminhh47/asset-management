package com.nashtech.assetmanagement.mapper;

import com.nashtech.assetmanagement.dto.response.RequestReturningResponseDto;
import com.nashtech.assetmanagement.entities.RequestReturning;
import com.nashtech.assetmanagement.enums.RequestReturningState;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RequestReturningMapper {

	@Autowired
	private ModelMapper modelMapper;

	public List<RequestReturningResponseDto> mapperListRequestReturning(List<RequestReturning> list) {
		return list.stream()
				.map(item -> modelMapper.map(item, RequestReturningResponseDto.class))
				.collect(Collectors.toList());
	}

	public List<RequestReturningState> mapperListStates(List<String> list) {
		return list.stream()
				.map(item -> modelMapper.map(item, RequestReturningState.class))
				.collect(Collectors.toList());
	}
	public List<RequestReturning> mapperListRequestReturningSort(List<RequestReturning> list) {
		return list.stream().sorted().collect(Collectors.toList());
	}
}
