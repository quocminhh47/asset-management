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
public class RequestReturningRequestGetListDto {
	
	List<String> states;
	String returnedDate;
	String keyword;
	String sortBy;
	String sortDirection;
	Integer page;
	Integer size;
	
}
