package com.nashtech.assetmanagement.service.impl;

import com.nashtech.assetmanagement.dto.request.UserRequestDto;
import com.nashtech.assetmanagement.dto.response.LocationResponseDTO;
import com.nashtech.assetmanagement.dto.response.ListUsersResponse;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.nashtech.assetmanagement.utils.AppConstants.DEFAULT_SORT_BY;
import static com.nashtech.assetmanagement.utils.AppConstants.DEFAULT_SORT_DIRECTION;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    UserServiceImpl userServiceImpl;

    @Test
    void createNewUser_ShouldReturnUserDto_WhenRequestValid() {
        Location location = mock(Location.class);
        Role role = mock(Role.class);
        Users user = mock(Users.class);
        UserRequestDto userRequest = mock(UserRequestDto.class);
        UserDto userResponse = mock(UserDto.class);
        when(locationRepository.findByName(userRequest.getLocationName())).thenReturn(Optional.of(location));
        when(roleRepository.findByName(userRequest.getRoleName())).thenReturn(Optional.of(role));
        when(userMapper.MapToUser(userRequest, role, location)).thenReturn(user);
        when(user.getFirstName()).thenReturn("firstname");
        when(user.getLastName()).thenReturn("lastname");
        when(user.getBirthDate()).thenReturn(new Date());
        when(modelMapper.map(user, UserDto.class)).thenReturn(userResponse);
        UserDto result = userServiceImpl.createNewUser(userRequest);
        assertThat(result).isEqualTo(userResponse);
    }

    @Test
    void createNewUser_ShouldThrowResourceNotFoundEx_WhenRequestRoleNameIncorrect() {
        Location location = mock(Location.class);
        UserRequestDto userRequest = mock(UserRequestDto.class);
        when(locationRepository.findByName(userRequest.getLocationName())).thenReturn(Optional.of(location));
        when(roleRepository.findByName(userRequest.getRoleName())).thenReturn(Optional.empty());
        ResourceNotFoundException e = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userServiceImpl.createNewUser(userRequest));
        assertThat(e.getMessage()).isEqualTo("Role name not found");
    }

    @Test
    void createNewUser_ShouldThrowResourceNotFoundEx_WhenRequestLocationNameIncorrect() {
        Role role = mock(Role.class);
        UserRequestDto userRequest = mock(UserRequestDto.class);
        when(locationRepository.findByName(userRequest.getLocationName())).thenReturn(Optional.empty());
        when(roleRepository.findByName(userRequest.getRoleName())).thenReturn(Optional.of(role));
        ResourceNotFoundException e = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userServiceImpl.createNewUser(userRequest));
        assertThat(e.getMessage()).isEqualTo("Location name not found");
    }

    @Test
    void editUser_ShouldReturnUserDto_WhenStaffCodeAndRequestCorrect() {
        Role role = mock(Role.class);
        Users user = mock(Users.class);
        UserRequestDto userRequest = mock(UserRequestDto.class);
        UserDto userResponse = mock(UserDto.class);
        when(userRepository.findByStaffCode("sd0001")).thenReturn(Optional.of(user));
        when(roleRepository.findByName(userRequest.getRoleName())).thenReturn(Optional.of(role));
        when(modelMapper.map(user, UserDto.class)).thenReturn(userResponse);
        UserDto result = userServiceImpl.editUser(userRequest, "sd0001");
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
        List<UserDto> result = userService.getUsersByStaffCodeOrName("text","location");
        assertThat(result).isEqualTo(responseList);
    }


    //-----------------US574 start--------------
    @DisplayName("Get users order by first name asc when give sorting parameters")
    @Test
    void givenPageParameters_whenGetAllUserOrderByFirstNameAsc_thenReturnListUsersResponse() {
        //given
        ListUsersResponse expectedResponse = mock(ListUsersResponse.class);
        ;
        Users user = mock(Users.class);
        Pageable pageable = PageRequest.of(0, 1, defaultSorting(DEFAULT_SORT_BY, DEFAULT_SORT_DIRECTION));
        when(authenticationService.getUser()).thenReturn(user);
        when(user.getStaffCode()).thenReturn("SD0001");
        Location location = mock(Location.class);
        when(user.getLocation()).thenReturn(location);
        when(user.getLocation().getCode()).thenReturn("HCM");
        Page<Users> usersPage = mock(Page.class);
        when(userRepository.findAllByOrderByFirstNameAsc(pageable, "SD0001", "HCM"))
                .thenReturn(usersPage);
        when(usersContent.getUsersContent(usersPage)).thenReturn(expectedResponse);

        //when
        ListUsersResponse actualResponse =
                userServiceImpl.getAllUserOrderByFirstNameAsc(0, 1, DEFAULT_SORT_BY, DEFAULT_SORT_DIRECTION);
        //then
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @DisplayName("Get users by searching staffcode or fullname when give sorting parameters")
    @Test
    void givenSortingParameters_whenGetAllUsersBySearchingStaffCodeOrName_thenReturnListUsersResponse() {
        //given
        ListUsersResponse expectedResponse = mock(ListUsersResponse.class);

        Users user = mock(Users.class);
        Location location = mock(Location.class);
        Pageable pageable = PageRequest.of(0, 1, defaultSorting(DEFAULT_SORT_BY, DEFAULT_SORT_DIRECTION));
        when(authenticationService.getUser()).thenReturn(user);
        when(user.getStaffCode()).thenReturn("SD0001");
        when(user.getLocation()).thenReturn(location);
        when(user.getLocation().getCode()).thenReturn("HN");
        Page<Users> usersPage = mock(Page.class);
        when(userRepository.searchByStaffCodeOrName(
                "SD0002".replaceAll(" ", "").toLowerCase(),
                "SD0001".replaceAll(" ", "").toLowerCase(),
                "HN".toLowerCase(),
                pageable)).thenReturn(usersPage);
        when(usersContent.getUsersContent(usersPage)).thenReturn(expectedResponse);

        //when
        ListUsersResponse actualResponse =
                userServiceImpl.getAllUsersBySearchingStaffCodeOrName(
                        0,
                        1,
                        DEFAULT_SORT_BY,
                        DEFAULT_SORT_DIRECTION,
                        "SD0002");

        //then
        assertThat(actualResponse).isEqualTo(expectedResponse);

    }

    @DisplayName("Get users by role when give sorting parameters positive case")
    @Test
    void givenValidRoleAndSortingParameters_whenGetUsersByRole_thenReturnListUsersResponse() {
        //given
        Role existRole = mock(Role.class);
        Users userLogged = mock(Users.class);
        Location location = mock(Location.class);
        Page<Users> usersPage = mock(Page.class);
        ListUsersResponse expectedResponse = mock(ListUsersResponse.class);

        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(existRole));
        when(authenticationService.getUser()).thenReturn(userLogged);
        when(userLogged.getStaffCode()).thenReturn("SD001");
        when(userLogged.getLocation()).thenReturn(location);
        when(location.getCode()).thenReturn("HCM");
        Pageable pageable = PageRequest.of(0, 1, defaultSorting(DEFAULT_SORT_BY, DEFAULT_SORT_DIRECTION));
        when(userRepository.findUsersByRole(pageable, existRole, "SD001", "HCM"))
                .thenReturn(usersPage);
        when(usersContent.getUsersContent(usersPage)).thenReturn(expectedResponse);

        //when
        ListUsersResponse actualResponse = userServiceImpl.getAllUsersByRole(
                0,
                1,
                DEFAULT_SORT_BY,
                DEFAULT_SORT_DIRECTION,
                "ADMIN");
        //then
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @DisplayName("Get users by role when give sorting parameters negative case")
    @Test
    void givenInvalidRole_whenGetUsersByRole_thenThrowsException() {
        //given
        when(roleRepository.findByName("NON_EXIST_ROLE")).thenReturn(Optional.empty());
        //when
        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userServiceImpl.getAllUsersByRole(0,1,DEFAULT_SORT_BY, DEFAULT_SORT_DIRECTION, "NON_EXIST_ROLE"));
        //then
        assertThat(exception.getMessage()).isEqualTo("Role." + "NON_EXIST_ROLE" + ".not.found");
    }


    public Sort defaultSorting(String sortBy, String sortDirection) {
        return sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
    }

    //-----------------US574 end--------------

}
