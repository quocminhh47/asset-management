package com.nashtech.assetmanagement.service.impl;

import com.nashtech.assetmanagement.dto.request.RequestLoginDTO;
import com.nashtech.assetmanagement.dto.request.UserRequestDto;
import com.nashtech.assetmanagement.dto.response.*;
import com.nashtech.assetmanagement.entities.Location;
import com.nashtech.assetmanagement.entities.Role;
import com.nashtech.assetmanagement.entities.Users;
import com.nashtech.assetmanagement.enums.UserState;
import com.nashtech.assetmanagement.exception.ResourceNotFoundException;
import com.nashtech.assetmanagement.mapper.LocationMapper;
import com.nashtech.assetmanagement.mapper.UserMapper;
import com.nashtech.assetmanagement.mapper.UsersContent;
import com.nashtech.assetmanagement.repositories.LocationRepository;
import com.nashtech.assetmanagement.repositories.RoleRepository;
import com.nashtech.assetmanagement.repositories.UserRepository;
import com.nashtech.assetmanagement.sercurity.jwt.JwtUtils;
import com.nashtech.assetmanagement.sercurity.userdetail.UserPrinciple;
import com.nashtech.assetmanagement.service.AuthenticationService;
import com.nashtech.assetmanagement.service.RoleService;
import com.nashtech.assetmanagement.service.UserService;
import com.nashtech.assetmanagement.utils.UserGenerateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UsersContent usersContent, RoleRepository roleRepository, UserMapper userMapper, AuthenticationManager authenticationManager, JwtUtils jwtUtils,
                           PasswordEncoder passwordEncoder, RoleService roleService,
                           LocationRepository locationRepository, LocationMapper locationMapper, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.usersContent = usersContent;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.authenticationService = authenticationService;
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
    }

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
        return new ResponseSignInDTO(userPrinciple.getStaffCode(),
                userPrinciple.getUsername(),userPrinciple.getState(), userPrinciple.getAuthorities(),
                token);
    }
    @Override
    public void createNewUser(UserRequestDto user) {
        if (!locationRepository.existsByName(user.getLocationName())){
            throw new ResourceNotFoundException("Location name not found");
        }
        Location location = locationRepository.findByName(user.getLocationName());
        Users newUser = UserMapper.MapToUser(user,roleService.getRole(user.getRoleName()),location);
        newUser.setState(UserState.ACTIVE);
        newUser.setStaffCode(UserGenerateUtil.generateStaffCode(userRepository.countUsersByStaffCode()));
        int sameName = userRepository.countUsersByFirstNameAndLastName(newUser.getFirstName(),newUser.getLastName());
        newUser.setUserName(UserGenerateUtil.generateUserName(newUser.getFirstName(),newUser.getLastName(),sameName));
        newUser.setPassword(UserGenerateUtil.generatePassword(newUser.getUserName(),newUser.getBirthDate()));
        userRepository.save(newUser);
    }

    @Override
    public void editUser(UserRequestDto user, String staffCode) {
        Optional<Users> usersOptional = userRepository.findByStaffCode(staffCode);
        if (usersOptional.isEmpty()){
            throw new ResourceNotFoundException("Staff code not found");
        }
        Users users = usersOptional.get();
        userMapper.requestDtoToUser(users,user,roleService.getRole(user.getRoleName()));
        userRepository.save(users);
    }

    @Override
    public LocationResponseDTO getLocationByStaffCode(String staffCode) {
        Optional<Users> usersOptional = userRepository.findByStaffCode(staffCode);
        if (usersOptional.isEmpty()){
            throw new ResourceNotFoundException("Staff code not found");
        }
        Users users = usersOptional.get();
        Location location = users.getLocation();
        return locationMapper.locationToLocationDTO(location);
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

}
