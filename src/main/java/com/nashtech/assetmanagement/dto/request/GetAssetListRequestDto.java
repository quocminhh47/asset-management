package com.nashtech.assetmanagement.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
