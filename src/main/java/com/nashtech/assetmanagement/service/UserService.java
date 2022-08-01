package com.nashtech.assetmanagement.service;

import com.nashtech.assetmanagement.dto.request.RequestChangePassDto;
import com.nashtech.assetmanagement.dto.request.RequestLoginDTO;
import com.nashtech.assetmanagement.dto.request.RequestUserDto;
import com.nashtech.assetmanagement.dto.response.*;
import com.nashtech.assetmanagement.entities.Users;
import com.nashtech.assetmanagement.sercurity.userdetail.UserPrinciple;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService {

    boolean isUsernameExist(String username);


    ResponseSignInDTO signIn(RequestLoginDTO requestLoginDTO);
    UserDto createNewUser(RequestUserDto user);

    UserDto editUser(RequestUserDto user, String staffCode);
    LocationResponseDTO getLocationByStaffCode(String staffCode);
    List<UserDto> getUsersByStaffCodeOrNameAndLocationCode(String text,String locationCode);

    UserPrinciple loadUserByUsername(String userName)
            throws UsernameNotFoundException;

    ListUsersResponse getAllUserOrderByFirstNameAsc(int pageNo,
                                                    int pageSize,
                                                    String sortBy,
                                                    String sortDirection);

    ListUsersResponse getAllUsersBySearchingStaffCodeOrName(int pageNo,
                                                            int pageSize,
                                                            String sortBy,
                                                            String sortDirection,
                                                            String searchText);

    //filter + searching
    ListUsersResponse getAllUsersBySearchingStaffCodeOrNameOrRole(int pageNo,
                                                                  int pageSize,
                                                                  String sortBy,
                                                                  String sortDirection,
                                                                  String searchText,
                                                                  String roleName);



    ListUsersResponse getAllUsersByRole(int pageNo,
                                                            int pageSize,
                                                            String sortBy,
                                                            String sortDirection,
                                                            String searchText);

    SingleUserResponse getUserDetailInfo(String staffCode);

    ResponseMessage changePasswordFirstLogin(String userName, String newPassword);
    ResponseUserDTO changePassword(RequestChangePassDto dto);

    void checkExistsByAssignedToOrAssignedBy(String staffCode);
    
    ResponseUserDTO disableStaff(String staffCode);

}
