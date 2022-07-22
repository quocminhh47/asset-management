package com.nashtech.assetmanagement.service.impl;

import com.nashtech.assetmanagement.dto.request.RequestLoginDTO;
import com.nashtech.assetmanagement.dto.response.ResponseSignInDTO;
import com.nashtech.assetmanagement.dto.response.ResponseUserDTO;
import com.nashtech.assetmanagement.entities.Users;
import com.nashtech.assetmanagement.exception.ResourceNotFoundException;
import com.nashtech.assetmanagement.mapper.UserMapper;
import com.nashtech.assetmanagement.repositories.UserRepository;
import com.nashtech.assetmanagement.sercurity.jwt.JwtUtils;
import com.nashtech.assetmanagement.sercurity.userdetail.UserPrinciple;
import com.nashtech.assetmanagement.service.RoleService;
import com.nashtech.assetmanagement.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final PasswordEncoder passwordEncoder;

    private final RoleService roleService;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, AuthenticationManager authenticationManager, JwtUtils jwtUtils, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
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
                userPrinciple.getUsername(),userPrinciple.getState(),
                userPrinciple.getAuthorities(),
                token);

    }
    @Override
    public UserPrinciple loadUserByUsername(String userName)
            throws UsernameNotFoundException {
        Users user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Did not find user has username = " + userName));
        return UserPrinciple.build(user);
    }

}
