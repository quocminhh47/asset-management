package com.nashtech.assetmanagement.dto.request;


import com.nashtech.assetmanagement.entities.AssignmentId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestAssignmentDTO {
    String assignedBy;
    String assignedTo;
    String assetCode;
    private Date assignedDate;
    private String note;
}
