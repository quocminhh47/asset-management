package com.nashtech.assetmanagement.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.nashtech.assetmanagement.dto.response.AssignmentDto;
import com.nashtech.assetmanagement.entities.Assignment;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class AssignmentMapper {

    private final ModelMapper mapper;

    public List<AssignmentDto> mapperListAssignment(List<Assignment> list) {
		List<AssignmentDto> result = list.stream().map(item -> mapper.map(item, AssignmentDto.class))
				.collect(Collectors.toList());
		return result;
	}
}
