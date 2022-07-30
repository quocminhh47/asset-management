package com.nashtech.assetmanagement.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListAssetResponseDto {
	
	private List<AssetResponseDto> list;
	private Long totalPages;
	
}
