package com.nashtech.assetmanagement.service.impl;

import com.nashtech.assetmanagement.dto.request.RequestLoginDTO;
import com.nashtech.assetmanagement.dto.request.UserRequestDto;
import com.nashtech.assetmanagement.dto.response.LocationResponseDTO;
import com.nashtech.assetmanagement.dto.response.ResponseSignInDTO;
import com.nashtech.assetmanagement.dto.response.ResponseUserDTO;
import com.nashtech.assetmanagement.entities.Location;
import com.nashtech.assetmanagement.entities.Users;
import com.nashtech.assetmanagement.exception.ResourceNotFoundException;
import com.nashtech.assetmanagement.mapper.LocationMapper;
import com.nashtech.assetmanagement.mapper.UserMapper;
import com.nashtech.assetmanagement.repositories.LocationRepository;
import com.nashtech.assetmanagement.repositories.UserRepository;
import com.nashtech.assetmanagement.sercurity.jwt.JwtUtils;
import com.nashtech.assetmanagement.sercurity.userdetail.UserPrinciple;
import com.nashtech.assetmanagement.service.RoleService;
import com.nashtech.assetmanagement.service.UserService;
import com.nashtech.assetmanagement.utils.UserGenerateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final PasswordEncoder passwordEncoder;

    private final RoleService roleService;

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, AuthenticationManager authenticationManager, JwtUtils jwtUtils,
                           PasswordEncoder passwordEncoder, RoleService roleService,
                           LocationRepository locationRepository, LocationMapper locationMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
    }

    @Override
    public boolean isUsernameExist(String username) {
        return userRepository.existsByUserName(username);
    }

    @Override
    public ResponseUserDTO createUser() {
        Users user=new Users();
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
                userPrinciple.getUsername(), userPrinciple.getAuthorities(),
                token);

    }
    @Override
    public void createNewUser(UserRequestDto user) {
        if (!locationRepository.existsByName(user.getLocationName())){
            throw new ResourceNotFoundException("Location name not found");
        }
        Location location = locationRepository.findByName(user.getLocationName());
        Users newUser = UserMapper.MapToUser(user,roleService.getRole(user.getRoleName()),location);
        newUser.setState(true);
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


}
