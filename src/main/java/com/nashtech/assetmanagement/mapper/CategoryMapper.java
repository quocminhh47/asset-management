package com.nashtech.assetmanagement.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nashtech.assetmanagement.dto.response.CategoryResponseDto;
import com.nashtech.assetmanagement.entities.Category;

@Component
public class CategoryMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public List<CategoryResponseDto> mapperListCategory(List<Category> list) {
		List<CategoryResponseDto> result = list.stream().map(item -> modelMapper.map(item, CategoryResponseDto.class))
				.collect(Collectors.toList());
		return result;
	}
	
}
