package com.nashtech.assetmanagement.service.impl;

import com.nashtech.assetmanagement.dto.request.RequestFirstLogin;
import com.nashtech.assetmanagement.dto.request.RequestLoginDTO;
import com.nashtech.assetmanagement.dto.response.*;
import com.nashtech.assetmanagement.entities.Role;
import com.nashtech.assetmanagement.entities.Users;
import com.nashtech.assetmanagement.enums.UserState;
import com.nashtech.assetmanagement.exception.ResourceNotFoundException;
import com.nashtech.assetmanagement.mapper.UserMapper;
import com.nashtech.assetmanagement.mapper.UsersContent;
import com.nashtech.assetmanagement.repositories.RoleRepository;
import com.nashtech.assetmanagement.repositories.UserRepository;
import com.nashtech.assetmanagement.sercurity.jwt.JwtUtils;
import com.nashtech.assetmanagement.sercurity.userdetail.UserPrinciple;
import com.nashtech.assetmanagement.service.AuthenticationService;
import com.nashtech.assetmanagement.service.RoleService;
import com.nashtech.assetmanagement.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UsersContent usersContent;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final AuthenticationService authenticationService;

    @Override
    public boolean isUsernameExist(String username) {
        return userRepository.existsByUserName(username);
    }

    @Override
    public ResponseUserDTO createUser() {
        Users user = new Users();
        user.setUserName("ducinox2000");
        user.setPassword("123456");
        user.setStaffCode("SD001");
        user.setRole(roleService.getRole(1L));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.userToResponseUser(userRepository.save(user));
    }

    @Override
    public ResponseSignInDTO signIn(RequestLoginDTO requestLoginDTO) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestLoginDTO.getUserName(), requestLoginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateJwtToken(authentication);
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        return new ResponseSignInDTO(
                userPrinciple.getStaffCode(),
                userPrinciple.getUsername(),
                userPrinciple.getState(),
                userPrinciple.getAuthorities(),
                token);

    }

    @Override
    public ListUsersResponse getAllUserOrderByFirstNameAsc(int pageNo,
                                                           int pageSize,
                                                           String sortBy,
                                                           String sortDirection) {

        Pageable pageable = PageRequest.of(pageNo, pageSize, defaultSorting(sortBy, sortDirection));

        Users user = authenticationService.getUser();
        String loggedStaffCode = user.getStaffCode();
        String location = user.getLocation().getCode();
        Page<Users> users = userRepository.findAllByOrderByFirstNameAsc(pageable, loggedStaffCode, location);

        return usersContent.getUsersContent(users);
    }

    @Override
    public ListUsersResponse getAllUsersBySearchingStaffCodeOrName(int pageNo,
                                                                   int pageSize,
                                                                   String sortBy,
                                                                   String sortDirection,
                                                                   String searchText) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, defaultSorting(sortBy, sortDirection));
        Users user = authenticationService.getUser();
        String loggedStaffCode = user.getStaffCode();
        String location = user.getLocation().getCode();
        Page<Users> users = userRepository.searchByStaffCodeOrName(
                searchText.replaceAll(" ", ""), loggedStaffCode.replaceAll(" ", ""), location, pageable);

        return usersContent.getUsersContent(users);
    }

    @Override
    public UserPrinciple loadUserByUsername(String userName)
            throws UsernameNotFoundException {
        Users user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Did not find user has username = " + userName));
        return UserPrinciple.build(user);
    }

    @Override
    public ListUsersResponse getAllUsersByRole(int pageNo,
                                               int pageSize,
                                               String sortBy,
                                               String sortDirection,
                                               String roleName) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role " + roleName + " not found"));

        Users user = authenticationService.getUser();
        String loggedStaffCode = user.getStaffCode();
        String location = user.getLocation().getCode();
        Pageable pageable = PageRequest.of(pageNo, pageSize, defaultSorting(sortBy, sortDirection));
        Page<Users> users = userRepository.findUsersByRole(pageable, role, loggedStaffCode, location);

        return usersContent.getUsersContent(users);
    }

    @Override
    public SingleUserResponse getUserDetailInfo(String staffCode) {
        Users user = userRepository.findById(staffCode)
                .orElseThrow(() -> new ResourceNotFoundException("User " + staffCode + " not found"));

        return userMapper.convertUserEntityToSingleUserResponse(user);
    }

    public Sort defaultSorting(String sortBy, String sortDirection) {
        return sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
    }

    @Override
    public ResponseMessage changePasswordFirstLogin(String userName, String newPassword) {
        Users user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("Did not find user " +
                        "with username: " + userName));
        if (user.getState() != UserState.INIT) {
            return new ResponseMessage(HttpStatus.CONFLICT, "You don't have to " +
                    "change your password for the first time you log in because your " +
                    "password has already been changed.", new Date());
        }
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            return new ResponseMessage(HttpStatus.CONFLICT, "The new password must be " +
                    "different from the previous password.", new Date());
        } else {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setState(UserState.ACTIVE);
        }
        return new ResponseMessage(HttpStatus.OK, "Change password successfully.",
                new Date());
    }
}
