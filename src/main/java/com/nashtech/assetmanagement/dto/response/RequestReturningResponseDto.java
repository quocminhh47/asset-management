package com.nashtech.assetmanagement.dto.response;

import java.sql.Date;

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
	private String requestedByUsername;
	private String acceptedByUsername;
	private Date returnedDate;
	private String state;
	private String assignmentAssetCode;
	private String assignmentAssetName;
	private String assignmentIdAssigneddate;
	@JsonProperty("assignedTo")
	private String assignmentIdAssignedTo;



}
