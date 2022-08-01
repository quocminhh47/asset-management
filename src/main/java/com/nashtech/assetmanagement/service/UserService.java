package com.nashtech.assetmanagement.service;

import com.nashtech.assetmanagement.dto.request.RequestChangePassDto;
import com.nashtech.assetmanagement.dto.request.RequestLoginDTO;
import com.nashtech.assetmanagement.dto.request.UserRequestDto;
import com.nashtech.assetmanagement.dto.response.*;
import com.nashtech.assetmanagement.entities.Users;
import com.nashtech.assetmanagement.sercurity.userdetail.UserPrinciple;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {

    boolean isUsernameExist(String username);


    ResponseSignInDTO signIn(RequestLoginDTO requestLoginDTO);
    UserDto createNewUser(UserRequestDto user);

    UserDto editUser(UserRequestDto user,String staffCode);
    LocationResponseDTO getLocationByStaffCode(String staffCode);

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
