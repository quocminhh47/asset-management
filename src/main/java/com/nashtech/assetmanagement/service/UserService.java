package com.nashtech.assetmanagement.service;

import com.nashtech.assetmanagement.dto.request.RequestLoginDTO;
import com.nashtech.assetmanagement.dto.request.UserRequestDto;
import com.nashtech.assetmanagement.dto.response.LocationResponseDTO;
import com.nashtech.assetmanagement.dto.response.ResponseSignInDTO;
import com.nashtech.assetmanagement.dto.response.ResponseUserDTO;
import com.nashtech.assetmanagement.entities.Users;

public interface UserService {
    boolean isUsernameExist(String username);


    ResponseUserDTO createUser();

    ResponseSignInDTO signIn(RequestLoginDTO requestLoginDTO);
    void createNewUser(UserRequestDto user);

    void editUser(UserRequestDto user,String staffCode);
    LocationResponseDTO getLocationByStaffCode(String staffCode);
}
