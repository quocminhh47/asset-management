package com.nashtech.assetmanagement.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nashtech.assetmanagement.dto.response.RequestReturningResponseDto;
import com.nashtech.assetmanagement.entities.RequestReturning;

@Component
public class RequestReturningMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public List<RequestReturningResponseDto> mapperListRequestReturning(List<RequestReturning> list) {
        List<RequestReturningResponseDto> result = list.stream().map(item -> modelMapper.map(item, RequestReturningResponseDto.class))
                .collect(Collectors.toList());
        return result;
    }
}