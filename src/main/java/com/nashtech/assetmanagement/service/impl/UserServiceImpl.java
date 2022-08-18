package com.nashtech.assetmanagement.service.impl;

import com.nashtech.assetmanagement.dto.request.ChangePassRequestDto;
import com.nashtech.assetmanagement.dto.request.UserRequestDto;
import com.nashtech.assetmanagement.dto.response.*;
import com.nashtech.assetmanagement.entities.Location;
import com.nashtech.assetmanagement.entities.Role;
import com.nashtech.assetmanagement.entities.Users;
import com.nashtech.assetmanagement.enums.UserState;
import com.nashtech.assetmanagement.exception.DateInvalidException;
import com.nashtech.assetmanagement.exception.ResourceNotFoundException;
import com.nashtech.assetmanagement.mapper.LocationMapper;
import com.nashtech.assetmanagement.mapper.UserMapper;
import com.nashtech.assetmanagement.mapper.UsersContent;
import com.nashtech.assetmanagement.repositories.AssignmentRepository;
import com.nashtech.assetmanagement.repositories.LocationRepository;
import com.nashtech.assetmanagement.repositories.RoleRepository;
import com.nashtech.assetmanagement.repositories.UserRepository;
import com.nashtech.assetmanagement.service.AuthenticationServices;
import com.nashtech.assetmanagement.service.UserService;
import com.nashtech.assetmanagement.utils.UserGenerateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UsersContent usersContent;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationServices authenticationService;
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;
    private final ModelMapper modelMapper;
    private final  AssignmentRepository assignmentRepository;


    @Override
    public UserContentResponseDto createNewUser(UserRequestDto user) {

        Optional<Location> location = locationRepository.findByName(user.getLocationName());
        Optional<Role> role = roleRepository.findByName(user.getRoleName());
        if (location.isEmpty()) {
            throw new ResourceNotFoundException("Location name not found");
        }
        if (role.isEmpty()) {
            throw new ResourceNotFoundException("Role name not found");
        }
        Users newUser = userMapper.mapToUser(user, role.get(), location.get());
        List<String> staffCodeList = userRepository.findAllStaffCode();
        int biggestStaffCode = UserGenerateUtil.getBiggestStaffCode(staffCodeList);
        String pattern = newUser.getLastName().substring(0, 1);
        int sameName = userRepository.countUsersByFirstNameAndLastNameLikeIgnoreCase(newUser.getFirstName(), (pattern + "%"));
        newUser.setState(UserState.INIT);
        newUser.setStaffCode(UserGenerateUtil.generateStaffCode(biggestStaffCode));
        newUser.setUserName(UserGenerateUtil.generateUserName(newUser.getFirstName(), newUser.getLastName(), sameName));
        newUser.setPassword(new BCryptPasswordEncoder()
                .encode(UserGenerateUtil.generatePassword(newUser.getUserName(), newUser.getBirthDate())));
        userRepository.save(newUser);
        return modelMapper.map(newUser, UserContentResponseDto.class);
    }

    @Override
    public UserContentResponseDto editUser(UserRequestDto user, String staffCode) {
        Optional<Users> usersOptional = userRepository.findByStaffCode(staffCode);
        Optional<Role> roleOptional = roleRepository.findByName(user.getRoleName());
        if (usersOptional.isEmpty()) {
            throw new ResourceNotFoundException("Staff code not found");
        }
        if (roleOptional.isEmpty()) {
            throw new ResourceNotFoundException("Role name not found");
        }
        Users editUser = usersOptional.get();
        Role role = roleOptional.get();
        userMapper.requestDtoToUser(editUser, user, role);
        userRepository.save(editUser);
        return modelMapper.map(editUser, UserContentResponseDto.class);
    }

    @Override
    public LocationResponseDto getLocationByStaffCode(String staffCode) {
        Optional<Users> usersOptional = userRepository.findByStaffCode(staffCode);
        if (usersOptional.isEmpty()) {
            throw new ResourceNotFoundException("Staff code not found");
        }
        Users users = usersOptional.get();
        Location location = users.getLocation();
        return locationMapper.locationToLocationDTO(location);
    }

    @Override
    public ListSearchingUserResponseDto getUsersByStaffCodeOrNameAndLocationCode(String text) {
        Users users = authenticationService.getUser();
        Location location = users.getLocation();
        List<Users> usersList = userRepository.findByStaffCodeOrNameAndLocationCode(text.toLowerCase(), location.getCode());
        return userMapper.mapListUserToListUserDto(usersList);
    }


    @Override
    public ListUsersResponseDto getAllUsersBySearchingStaffCodeOrNameOrRole(int pageNo,
                                                                            int pageSize,
                                                                            String sortBy,
                                                                            String sortDirection,
                                                                            String searchText,
                                                                            List<String> rolesName) {

        Users user = authenticationService.getUser();
        String loggedStaffCode = user.getStaffCode();
        String location = user.getLocation().getCode();
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<Users> users = userRepository.searchByStaffCodeOrNameWithRole(
                searchText.toLowerCase(),
                loggedStaffCode.replaceAll(" ", ""),
                location.toLowerCase(),
                rolesName,
                pageable
        );

        return usersContent.getUsersContent(users);
    }

    @Override
    public SingleUserResponseDto getUserDetailInfo(String staffCode) {
        Users user = userRepository.findById(staffCode)
                .orElseThrow(() -> new ResourceNotFoundException("User " + staffCode + " not found"));

        return userMapper.convertUserEntityToSingleUserResponse(user);
    }


    @Override
    public MessageResponse changePasswordFirstLogin(String userName, String newPassword) {
        Users user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("Did not find user " + "with username: " + userName));
        if (user.getState() != UserState.INIT) {
            return new MessageResponse(HttpStatus.CONFLICT,
                    "You don't have to " + "change your password for the first time you log in because your "
                            + "password has already been changed.",
                    new Date());
        }
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            return new MessageResponse(HttpStatus.CONFLICT,
                    "The new password must be " + "different from the previous password.", new Date());
        } else {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setState(UserState.ACTIVE);
            userRepository.save(user);
        }
        return new MessageResponse(HttpStatus.OK, "Change password successfully.", new Date());
    }

    @Override
    public UserResponseDto changePassword(ChangePassRequestDto dto) {
        Optional<Users> optional = userRepository.findById(dto.getStaffCode());
        if (optional.isEmpty()) {
            throw new ResourceNotFoundException(String.format("user.not.found.with.staff.code:%s", dto.getStaffCode()));
        }
        Users user = optional.get();
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new ResourceNotFoundException("Password is incorrect");
        } else if (passwordEncoder.matches(dto.getNewPassword(), user.getPassword())) {
            throw new ResourceNotFoundException("You entered an old password");
        } else {
            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
            user = userRepository.save(user);
            return modelMapper.map(user, UserResponseDto.class);
        }
    }

    @Override
    public void checkExistsAssignment(String staffCode) {
        Optional<Users> optional = userRepository.findById(staffCode);
        if (optional.isEmpty()) {
            throw new ResourceNotFoundException(String.format("staff.not.found.with.code:%s", staffCode));
        }
        Users user = optional.get();
        boolean existInAssignment = assignmentRepository.existsByAssignedToOrAssignedBy(user, user);
        if (existInAssignment) {
            throw new DateInvalidException("exist.valid.assignments");
        }
    }

    public UserResponseDto disableStaff(String staffCode) {
        Optional<Users> optional = userRepository.findById(staffCode);
        if (optional.isEmpty()) {
            throw new ResourceNotFoundException(String.format("staff.not.found.with.code:%s", staffCode));
        }
        Users user = optional.get();
        user.setState(UserState.INACTIVE);
        userRepository.save(user);
        return modelMapper.map(user, UserResponseDto.class);
    }
}
