package com.nashtech.assetmanagement.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nashtech.assetmanagement.dto.response.AssetResponseDto;
import com.nashtech.assetmanagement.entities.Asset;

@Component
public class AssetMapper {

	@Autowired
	private ModelMapper modelMapper;
	
	public List<AssetResponseDto> mapperListAsset(List<Asset> list) {
		List<AssetResponseDto> result = list.stream().map(item -> modelMapper.map(item, AssetResponseDto.class))
				.collect(Collectors.toList());
		return result;
	}
}
