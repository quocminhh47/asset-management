package com.nashtech.assetmanagement.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestReturningResponseDto {
	
	private Long id;
	
	@JsonProperty("requestedBy")
	private String requestedByUsername;
	
	@JsonProperty("acceptedBy")
	private String acceptedByUsername;
	
	@JsonProperty("assignedTo")
    private String assignmentIdAssignedTo;
	
	private String returnedDate;
	private String state;
	
	@JsonProperty("assetCode")
	private String assignmentAssetCode;
	
	@JsonProperty("assetName")
	private String assignmentAssetName;
	
	@JsonProperty("assignedDate")
	private String assignmentIdAssigneddate;
	
}
