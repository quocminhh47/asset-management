package com.nashtech.assetmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestChangePassDto {
	private String staffCode;
	@Size(min = 8)
	private String password;
	@Size(min = 8)
	private String newPassword;
}
