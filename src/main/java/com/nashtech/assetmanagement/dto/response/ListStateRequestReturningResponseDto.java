package com.nashtech.assetmanagement.dto.response;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListStateRequestReturningResponseDto {
	Map<String, String> listStates;
}
