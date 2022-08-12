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
public class GetRequestReturningListRequestDto {

	private List<String> states;
	private String returnedDate;
	private String keyword;
	private String sortBy;
	private String sortDirection;
	private Integer page;
	private Integer size;

}
