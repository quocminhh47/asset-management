package com.nashtech.assetmanagement.service;

import com.nashtech.assetmanagement.dto.request.RequestChangePassDto;
import com.nashtech.assetmanagement.dto.request.RequestLoginDTO;
import com.nashtech.assetmanagement.dto.response.ResponseSignInDTO;
import com.nashtech.assetmanagement.dto.response.ResponseUserDTO;
import com.nashtech.assetmanagement.entities.Users;
import com.nashtech.assetmanagement.sercurity.userdetail.UserPrinciple;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {
    boolean isUsernameExist(String username);


    ResponseUserDTO createUser();

    ResponseSignInDTO signIn(RequestLoginDTO requestLoginDTO);

    UserPrinciple loadUserByUsername(String userName)
            throws UsernameNotFoundException;


	ResponseUserDTO changePassword(RequestChangePassDto dto);
}
