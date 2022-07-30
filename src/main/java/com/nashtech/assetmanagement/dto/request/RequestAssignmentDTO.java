package com.nashtech.assetmanagement.dto.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.nashtech.assetmanagement.entities.AssignmentId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestAssignmentDTO {
    @NotEmpty
    String assignedBy;
    @NotEmpty
    String assignedTo;
    @NotEmpty
    String assetCode;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotEmpty
    private Date assignedDate;
    private String note;
}
