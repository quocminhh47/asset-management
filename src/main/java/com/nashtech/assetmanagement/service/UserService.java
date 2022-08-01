package com.nashtech.assetmanagement.service;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.nashtech.assetmanagement.dto.request.RequestChangePassDto;
import com.nashtech.assetmanagement.dto.request.RequestLoginDTO;
import com.nashtech.assetmanagement.dto.request.RequestUserDto;
import com.nashtech.assetmanagement.dto.response.ListUsersResponse;
import com.nashtech.assetmanagement.dto.response.LocationResponseDTO;
import com.nashtech.assetmanagement.dto.response.ResponseMessage;
import com.nashtech.assetmanagement.dto.response.ResponseSignInDTO;
import com.nashtech.assetmanagement.dto.response.ResponseUserDTO;
import com.nashtech.assetmanagement.dto.response.SingleUserResponse;
import com.nashtech.assetmanagement.dto.response.UserDto;
import com.nashtech.assetmanagement.sercurity.userdetail.UserPrinciple;

public interface UserService {

    boolean isUsernameExist(String username);


    ResponseSignInDTO signIn(RequestLoginDTO requestLoginDTO);
    UserDto createNewUser(RequestUserDto user);

    UserDto editUser(RequestUserDto user, String staffCode);
    LocationResponseDTO getLocationByStaffCode(String staffCode);
    List<UserDto> getUsersByStaffCodeOrNameAndLocationCode(String text,String locationCode);

    UserPrinciple loadUserByUsername(String userName)
            throws UsernameNotFoundException;


    //filter + searching
    ListUsersResponse getAllUsersBySearchingStaffCodeOrNameOrRole(int pageNo,
                                                                  int pageSize,
                                                                  String sortBy,
                                                                  String sortDirection,
                                                                  String searchText,
                                                                  List<String> roles);


    

    SingleUserResponse getUserDetailInfo(String staffCode);

    ResponseMessage changePasswordFirstLogin(String userName, String newPassword);
    ResponseUserDTO changePassword(RequestChangePassDto dto);

    void checkExistsAssignment(String staffCode);
    
    ResponseUserDTO disableStaff(String staffCode);

}
