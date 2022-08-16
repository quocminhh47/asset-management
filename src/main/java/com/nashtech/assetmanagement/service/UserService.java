package com.nashtech.assetmanagement.service;

import com.nashtech.assetmanagement.dto.request.ChangePassRequestDto;
import com.nashtech.assetmanagement.dto.request.UserRequestDto;
import com.nashtech.assetmanagement.dto.response.*;

import java.util.List;

public interface UserService {

    UserContentResponseDto createNewUser(UserRequestDto user);

    UserContentResponseDto editUser(UserRequestDto user, String staffCode);

    LocationResponseDto getLocationByStaffCode(String staffCode);

    ListSearchingUserResponseDto getUsersByStaffCodeOrNameAndLocationCode(String text);

    ListUsersResponseDto getAllUsersBySearchingStaffCodeOrNameOrRole(int pageNo,
                                                                     int pageSize,
                                                                     String sortBy,
                                                                     String sortDirection,
                                                                     String searchText,
                                                                     List<String> roles);


    SingleUserResponseDto getUserDetailInfo(String staffCode);

    MessageResponse changePasswordFirstLogin(String userName, String newPassword);

    UserResponseDto changePassword(ChangePassRequestDto dto);

    void checkExistsAssignment(String staffCode);

    UserResponseDto disableStaff(String staffCode);

}
