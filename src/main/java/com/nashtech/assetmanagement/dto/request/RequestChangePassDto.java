package com.nashtech.assetmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestChangePassDto {
	private String staffCode;
	private String password;
	private String newPassword;
}
