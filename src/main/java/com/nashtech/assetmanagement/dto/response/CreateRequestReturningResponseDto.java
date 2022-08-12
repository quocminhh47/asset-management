package com.nashtech.assetmanagement.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateRequestReturningResponseDto {
    private Long id;

    @JsonProperty("requestedByUsername")
    private String requestedByUsername;

    @JsonProperty("acceptedByUsername")
    private String acceptedByUsername;

    private Date returnedDate;

    private String state;

    // About assignment of this Request returning
    @JsonProperty("assignmentAssignedTo")
    private String assignmentIdAssignedTo;

    @JsonProperty("assignmentAssetCode")
    private String assignmentIdAssetCode;

    @JsonProperty("assignmentAssignedDate")
    private Date assignmentIdAssignedDate;

    @JsonProperty("assignmentAssignedByUsername")
    private String assignmentAssignedByUsername;

    @JsonProperty("assignmentState")
    private String assignmentState;

    @JsonProperty("assignmentNote")
    private String assignmentNote;
}