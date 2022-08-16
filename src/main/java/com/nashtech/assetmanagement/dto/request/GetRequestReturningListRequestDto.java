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
public class GetRequestReturningListRequestDto {

	private List<String> states;
	private String returnedDate;
	private String keyword;
	private String sortBy;
	private String sortDirection;
	private Integer page;
	private Integer size;

}
