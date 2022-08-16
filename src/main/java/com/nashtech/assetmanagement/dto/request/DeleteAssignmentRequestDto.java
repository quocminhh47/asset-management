package com.nashtech.assetmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.sql.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteAssignmentRequestDto {
    @NotEmpty(message = "AssignedTo must not be empty.")
    private String assignedTo;
    @NotEmpty(message = "AssetCode must not be empty.")
    private String assetCode;
    private Date assignedDate;
}
