package com.nashtech.assetmanagement.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditAssignmentRequestDto {
    @NotEmpty
    String assignedToStaffCode;
    @NotEmpty
    String assetCode;
    @NotEmpty
    String oldAssetCode;
    @NotEmpty
    String oldAssignedTo;
    @NotEmpty
    Date oldAssignedDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotEmpty
    Date assignedDate;
    String note;
}
