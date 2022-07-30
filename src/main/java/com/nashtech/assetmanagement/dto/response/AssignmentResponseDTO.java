package com.nashtech.assetmanagement.dto.response;


import com.nashtech.assetmanagement.entities.Asset;
import com.nashtech.assetmanagement.entities.AssignmentId;
import com.nashtech.assetmanagement.entities.Users;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentResponseDTO {

    private String assignedTo;
    private String assignedBy;
    private String assetCode;
    private String state;
    private String note;
    private Date assignedDate;
}
