package com.nashtech.assetmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetAssetListRequestDto {
	
	private String userId;
	private Integer page;
	private Integer size;
	private String keyword;
	private String sortBy;
	private String sortDirection;
	private List<String> categoryIds;
	private List<String> states;
	
}
