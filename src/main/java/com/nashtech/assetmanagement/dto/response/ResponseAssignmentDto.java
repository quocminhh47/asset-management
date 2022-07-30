package com.nashtech.assetmanagement.dto.response;


import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseAssignmentDto {

    private String assignedTo;
    private String assignedBy;
    private String assetCode;
    private String state;
    private String note;
    private Date assignedDate;
}
