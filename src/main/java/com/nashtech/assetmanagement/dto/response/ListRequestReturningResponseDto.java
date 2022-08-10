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
public class ListRequestReturningResponseDto {
	private List<RequestReturningResponseDto> list;
    private Long totalPages;
}
