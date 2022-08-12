package com.nashtech.assetmanagement.dto.response;

import java.util.HashMap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListStateRequestReturningResponseDto {
	HashMap<String, String> listStates;
}
