package com.nashtech.assetmanagement.dto.response;

import lombok.*;

import java.util.HashMap;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListStateRequestReturningResponseDto {
	HashMap<String, String> listStates;
}
