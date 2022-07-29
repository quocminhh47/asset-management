package com.nashtech.assetmanagement.service.impl;

import com.nashtech.assetmanagement.dto.request.UserRequestDto;
import com.nashtech.assetmanagement.dto.response.LocationResponseDTO;
import com.nashtech.assetmanagement.dto.response.UserDto;
import com.nashtech.assetmanagement.entities.Location;
import com.nashtech.assetmanagement.entities.Role;
import com.nashtech.assetmanagement.entities.Users;
import com.nashtech.assetmanagement.exception.ResourceNotFoundException;
import com.nashtech.assetmanagement.mapper.LocationMapper;
import com.nashtech.assetmanagement.mapper.UserMapper;
import com.nashtech.assetmanagement.mapper.UsersContent;
import com.nashtech.assetmanagement.repositories.LocationRepository;
import com.nashtech.assetmanagement.repositories.RoleRepository;
import com.nashtech.assetmanagement.repositories.UserRepository;
import com.nashtech.assetmanagement.sercurity.jwt.JwtUtils;
import com.nashtech.assetmanagement.service.AuthenticationService;
import com.nashtech.assetmanagement.utils.UserGenerateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    UserRepository userRepository;
    @Mock
    UsersContent usersContent;
    @Mock
    RoleRepository roleRepository;
    @Mock
    UserMapper userMapper;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    JwtUtils jwtUtils;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    AuthenticationService authenticationService;
    @Mock
    LocationRepository locationRepository;
    @Mock
    LocationMapper locationMapper;
    @Mock
    ModelMapper modelMapper;
    @InjectMocks
    UserServiceImpl userService;

    @Test
    void createNewUser_ShouldReturnUserDto_WhenRequestValid(){
        Location location = mock(Location.class);
        Role role = mock(Role.class);
        Users user = mock(Users.class);
        UserRequestDto userRequest = mock(UserRequestDto.class);
        UserDto userResponse = mock(UserDto.class);
        when(locationRepository.findByName(userRequest.getLocationName())).thenReturn(Optional.of(location));
        when(roleRepository.findByName(userRequest.getRoleName())).thenReturn(Optional.of(role));
        when(userMapper.MapToUser(userRequest,role,location)).thenReturn(user);
        when(user.getFirstName()).thenReturn("firstname");
        when(user.getLastName()).thenReturn("lastname");
        when(user.getBirthDate()).thenReturn(new Date());
        when(modelMapper.map(user,UserDto.class)).thenReturn(userResponse);
        UserDto result = userService.createNewUser(userRequest);
        assertThat(result).isEqualTo(userResponse);
    }
    @Test
    void createNewUser_ShouldThrowResourceNotFoundEx_WhenRequestRoleNameIncorrect(){
        Location location = mock(Location.class);
        UserRequestDto userRequest = mock(UserRequestDto.class);
        when(locationRepository.findByName(userRequest.getLocationName())).thenReturn(Optional.of(location));
        when(roleRepository.findByName(userRequest.getRoleName())).thenReturn(Optional.empty());
        ResourceNotFoundException e = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userService.createNewUser(userRequest));
        assertThat(e.getMessage()).isEqualTo("Role name not found");
    }
    @Test
    void createNewUser_ShouldThrowResourceNotFoundEx_WhenRequestLocationNameIncorrect(){
        Role role = mock(Role.class);
        UserRequestDto userRequest = mock(UserRequestDto.class);
        when(locationRepository.findByName(userRequest.getLocationName())).thenReturn(Optional.empty());
        when(roleRepository.findByName(userRequest.getRoleName())).thenReturn(Optional.of(role));
        ResourceNotFoundException e = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userService.createNewUser(userRequest));
        assertThat(e.getMessage()).isEqualTo("Location name not found");
    }
    @Test
    void editUser_ShouldReturnUserDto_WhenStaffCodeAndRequestCorrect(){
        Role role = mock(Role.class);
        Users user = mock(Users.class);
        UserRequestDto userRequest = mock(UserRequestDto.class);
        UserDto userResponse = mock(UserDto.class);
        when(userRepository.findByStaffCode("sd0001")).thenReturn(Optional.of(user));
        when(roleRepository.findByName(userRequest.getRoleName())).thenReturn(Optional.of(role));
        when(modelMapper.map(user,UserDto.class)).thenReturn(userResponse);
        UserDto result = userService.editUser(userRequest,"sd0001");
        assertThat(result).isEqualTo(userResponse);
    }
    @Test
    void editUser_ShouldThrowResourceNotFoundEx_WhenStaffCodeIncorrect(){
        UserRequestDto userRequest = mock(UserRequestDto.class);
        when(userRepository.findByStaffCode("sd0001")).thenReturn(Optional.empty());
        ResourceNotFoundException e = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userService.editUser(userRequest,"sd0001"));
        assertThat(e.getMessage()).isEqualTo("Staff code not found");
    }
    @Test
    void editUser_ShouldThrowResourceNotFoundEx_WhenRequestRoleIncorrect(){
        UserRequestDto userRequest = mock(UserRequestDto.class);
        Users user = mock(Users.class);
        when(userRepository.findByStaffCode("sd0001")).thenReturn(Optional.of(user));
        when(roleRepository.findByName(userRequest.getRoleName())).thenReturn(Optional.empty());
        ResourceNotFoundException e = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userService.editUser(userRequest,"sd0001"));
        assertThat(e.getMessage()).isEqualTo("Role name not found");
    }

    @Test
    void getLocationByStaffCode_ShouldReturnLocationName_WhenStaffCodeCorrect(){
        Users user = mock(Users.class);
        Location location = mock(Location.class);
        LocationResponseDTO response = mock(LocationResponseDTO.class);
        when(userRepository.findByStaffCode("sd0001")).thenReturn(Optional.of(user));
        when(user.getLocation()).thenReturn(location);
        when(locationMapper.locationToLocationDTO(location)).thenReturn(response);
        LocationResponseDTO result = userService.getLocationByStaffCode("sd0001");
        assertThat(result).isEqualTo(response);
    }
    @Test
    void getLocationByStaffCode_ShouldThrowResourceNotFoundEx_WhenStaffCodeIncorrect(){
        when(userRepository.findByStaffCode("sd0001")).thenReturn(Optional.empty());
        ResourceNotFoundException e = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userService.getLocationByStaffCode("sd0001"));
        assertThat(e.getMessage()).isEqualTo("Staff code not found");
    }
    @Test
    void getUserByStaffCodeOrName_ShouldReturnUserDtoList_WhenStaffCodeOrNameExist(){
        List<Users> usersList = mock(ArrayList.class);
        List<UserDto> responseList = mock(ArrayList.class);
        when(userRepository.findByStaffCodeAndName("text")).thenReturn(usersList);
        when(userMapper.mapListUserToListUserDto(usersList)).thenReturn(responseList);
        List<UserDto> result = userService.getUsersByStaffCodeOrName("text");
        assertThat(result).isEqualTo(responseList);
    }


}
