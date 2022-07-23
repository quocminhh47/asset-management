package com.nashtech.assetmanagement.service;


import com.nashtech.assetmanagement.dto.request.RequestLoginDTO;
import com.nashtech.assetmanagement.dto.response.*;
import com.nashtech.assetmanagement.sercurity.userdetail.UserPrinciple;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public interface UserService {

    boolean isUsernameExist(String username);

    ResponseUserDTO createUser();

    ResponseSignInDTO signIn(RequestLoginDTO requestLoginDTO);

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
}
