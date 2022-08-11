package com.nashtech.assetmanagement.dto.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;


@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReturningRequestDto {

    @NotEmpty(message = "Assigned to is required")
    private String assignedTo;

    @NotEmpty(message = "Asset code is required")
    private String assetCode;

    @NotEmpty(message = "Assigned Date is required")
    private String assignedDate;
}
