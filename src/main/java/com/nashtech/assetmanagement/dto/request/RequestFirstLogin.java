package com.nashtech.assetmanagement.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestFirstLogin {
    @NotEmpty
    private String userName;
    @Size(min = 8)
    private String newPassword;

}
