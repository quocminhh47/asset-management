package com.nashtech.assetmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseSignInDTO {
    private String staffCode;
    private String userName;
    private Collection<? extends GrantedAuthority> roles;
    private String token;

}

