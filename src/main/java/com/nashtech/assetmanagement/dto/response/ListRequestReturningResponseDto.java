package com.nashtech.assetmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListRequestReturningResponseDto {
	private List<RequestReturningResponseDto> list;
    private Long totalPages;
}
