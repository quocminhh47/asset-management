package com.nashtech.assetmanagement.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestFirstLogin {
    @NotEmpty
    private String userName;
    private String newPassword;

}
