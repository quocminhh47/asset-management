package com.nashtech.assetmanagement.service.impl;

import static com.nashtech.assetmanagement.utils.AppConstants.DEFAULT_SORT_BY;
import static com.nashtech.assetmanagement.utils.AppConstants.DEFAULT_SORT_DIRECTION;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.nashtech.assetmanagement.dto.request.RequestChangePassDto;
import com.nashtech.assetmanagement.dto.request.RequestUserDto;
import com.nashtech.assetmanagement.dto.response.ListUsersResponse;
import com.nashtech.assetmanagement.dto.response.LocationResponseDTO;
import com.nashtech.assetmanagement.dto.response.ResponseMessage;
import com.nashtech.assetmanagement.dto.response.ResponseUserDTO;
import com.nashtech.assetmanagement.dto.response.UserDto;
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
import com.nashtech.assetmanagement.sercurity.jwt.JwtUtils;
import com.nashtech.assetmanagement.service.AuthenticationService;

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
	
	@Mock
	AssignmentRepository assignmentRepository;

    @Test
    void createNewUser_ShouldReturnUserDto_WhenRequestValid(){
        Location location = mock(Location.class);
        Role role = mock(Role.class);
        Users user = mock(Users.class);
        RequestUserDto userRequest = mock(RequestUserDto.class);
        UserDto userResponse = mock(UserDto.class);
        when(locationRepository.findByName(userRequest.getLocationName())).thenReturn(Optional.of(location));
        when(roleRepository.findByName(userRequest.getRoleName())).thenReturn(Optional.of(role));
        when(userMapper.MapToUser(userRequest,role,location)).thenReturn(user);
        when(user.getFirstName()).thenReturn("firstname");
        when(user.getLastName()).thenReturn("lastname");
        when(user.getBirthDate()).thenReturn(new Date());
        when(modelMapper.map(user, UserDto.class)).thenReturn(userResponse);
        UserDto result = userServiceImpl.createNewUser(userRequest);
        assertThat(result).isEqualTo(userResponse);
    }
    @Test
    void createNewUser_ShouldThrowResourceNotFoundEx_WhenRequestRoleNameIncorrect(){
        Location location = mock(Location.class);
        RequestUserDto userRequest = mock(RequestUserDto.class);
        when(locationRepository.findByName(userRequest.getLocationName())).thenReturn(Optional.of(location));
        when(roleRepository.findByName(userRequest.getRoleName())).thenReturn(Optional.empty());
        ResourceNotFoundException e = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userServiceImpl.createNewUser(userRequest));
        assertThat(e.getMessage()).isEqualTo("Role name not found");
    }
    @Test
    void createNewUser_ShouldThrowResourceNotFoundEx_WhenRequestLocationNameIncorrect(){
        Role role = mock(Role.class);
        RequestUserDto userRequest = mock(RequestUserDto.class);
        when(locationRepository.findByName(userRequest.getLocationName())).thenReturn(Optional.empty());
        when(roleRepository.findByName(userRequest.getRoleName())).thenReturn(Optional.of(role));
        ResourceNotFoundException e = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userServiceImpl.createNewUser(userRequest));
        assertThat(e.getMessage()).isEqualTo("Location name not found");
    }
    @Test
    void editUser_ShouldReturnUserDto_WhenStaffCodeAndRequestCorrect(){
        Role role = mock(Role.class);
        Users user = mock(Users.class);
        RequestUserDto userRequest = mock(RequestUserDto.class);
        UserDto userResponse = mock(UserDto.class);
        when(userRepository.findByStaffCode("sd0001")).thenReturn(Optional.of(user));
        when(roleRepository.findByName(userRequest.getRoleName())).thenReturn(Optional.of(role));
        when(modelMapper.map(user, UserDto.class)).thenReturn(userResponse);
        UserDto result = userServiceImpl.editUser(userRequest, "sd0001");
        assertThat(result).isEqualTo(userResponse);
    }
    @Test
    void editUser_ShouldThrowResourceNotFoundEx_WhenStaffCodeIncorrect(){
        RequestUserDto userRequest = mock(RequestUserDto.class);
        when(userRepository.findByStaffCode("sd0001")).thenReturn(Optional.empty());
        ResourceNotFoundException e = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userServiceImpl.editUser(userRequest,"sd0001"));
        assertThat(e.getMessage()).isEqualTo("Staff code not found");
    }
    @Test
    void editUser_ShouldThrowResourceNotFoundEx_WhenRequestRoleIncorrect(){
        RequestUserDto userRequest = mock(RequestUserDto.class);
        Users user = mock(Users.class);
        when(userRepository.findByStaffCode("sd0001")).thenReturn(Optional.of(user));
        when(roleRepository.findByName(userRequest.getRoleName())).thenReturn(Optional.empty());
        ResourceNotFoundException e = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userServiceImpl.editUser(userRequest,"sd0001"));
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
        LocationResponseDTO result = userServiceImpl.getLocationByStaffCode("sd0001");
        assertThat(result).isEqualTo(response);
    }
    @Test
    void getLocationByStaffCode_ShouldThrowResourceNotFoundEx_WhenStaffCodeIncorrect(){
        when(userRepository.findByStaffCode("sd0001")).thenReturn(Optional.empty());
        ResourceNotFoundException e = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userServiceImpl.getLocationByStaffCode("sd0001"));
        assertThat(e.getMessage()).isEqualTo("Staff code not found");
    }
    @Test
    void getUserByStaffCodeOrName_ShouldReturnUserDtoList_WhenStaffCodeOrNameExist(){
        List<Users> usersList = mock(ArrayList.class);
        List<UserDto> responseList = mock(ArrayList.class);
        Location location = mock(Location.class);
        when(locationRepository.findById("HCM")).thenReturn(Optional.of(location));
        when(userRepository.findByStaffCodeOrNameAndLocationCode("text","HCM")).thenReturn(usersList);
        when(userMapper.mapListUserToListUserDto(usersList)).thenReturn(responseList);
        List<UserDto> result = userServiceImpl.getUsersByStaffCodeOrNameAndLocationCode("text","HCM");
        assertThat(result).isEqualTo(responseList);
    }


	// -----------------US574 start--------------
	@DisplayName("Get users order by first name asc when give sorting parameters")
	@Test
	void givenPageParameters_whenGetAllUserOrderByFirstNameAsc_thenReturnListUsersResponse() {
		// given
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
		when(userRepository.findAllByOrderByFirstNameAsc(pageable, "SD0001", "HCM")).thenReturn(usersPage);
		when(usersContent.getUsersContent(usersPage)).thenReturn(expectedResponse);

		// when
		ListUsersResponse actualResponse = userServiceImpl.getAllUserOrderByFirstNameAsc(0, 1, DEFAULT_SORT_BY,
				DEFAULT_SORT_DIRECTION);
		// then
		assertThat(actualResponse).isEqualTo(expectedResponse);
	}

	@DisplayName("Get users by searching staffcode or fullname when give sorting parameters")
	@Test
	void givenSortingParameters_whenGetAllUsersBySearchingStaffCodeOrName_thenReturnListUsersResponse() {
		// given
		ListUsersResponse expectedResponse = mock(ListUsersResponse.class);

		Users user = mock(Users.class);
		Location location = mock(Location.class);
		Pageable pageable = PageRequest.of(0, 1, defaultSorting(DEFAULT_SORT_BY, DEFAULT_SORT_DIRECTION));
		when(authenticationService.getUser()).thenReturn(user);
		when(user.getStaffCode()).thenReturn("SD0001");
		when(user.getLocation()).thenReturn(location);
		when(user.getLocation().getCode()).thenReturn("HN");
		Page<Users> usersPage = mock(Page.class);
		when(userRepository.searchByStaffCodeOrName("SD0002".replaceAll(" ", "").toLowerCase(),
				"SD0001".replaceAll(" ", "").toLowerCase(), "HN".toLowerCase(), pageable)).thenReturn(usersPage);
		when(usersContent.getUsersContent(usersPage)).thenReturn(expectedResponse);

		// when
		ListUsersResponse actualResponse = userServiceImpl.getAllUsersBySearchingStaffCodeOrName(0, 1, DEFAULT_SORT_BY,
				DEFAULT_SORT_DIRECTION, "SD0002");

		// then
		assertThat(actualResponse).isEqualTo(expectedResponse);

	}

	@DisplayName("Get users by role when give sorting parameters positive case")
	@Test
	void givenValidRoleAndSortingParameters_whenGetUsersByRole_thenReturnListUsersResponse() {
		// given
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
		when(userRepository.findUsersByRole(pageable, existRole, "SD001", "HCM")).thenReturn(usersPage);
		when(usersContent.getUsersContent(usersPage)).thenReturn(expectedResponse);

		// when
		ListUsersResponse actualResponse = userServiceImpl.getAllUsersByRole(0, 1, DEFAULT_SORT_BY,
				DEFAULT_SORT_DIRECTION, "ADMIN");
		// then
		assertThat(actualResponse).isEqualTo(expectedResponse);
	}

	@DisplayName("Get users by role when give sorting parameters negative case")
	@Test
	void givenInvalidRole_whenGetUsersByRole_thenThrowsException() {
		// given
		when(roleRepository.findByName("NON_EXIST_ROLE")).thenReturn(Optional.empty());
		// when
		ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class,
				() -> userServiceImpl.getAllUsersByRole(0, 1, DEFAULT_SORT_BY, DEFAULT_SORT_DIRECTION,
						"NON_EXIST_ROLE"));
		// then
		assertThat(exception.getMessage()).isEqualTo("Role." + "NON_EXIST_ROLE" + ".not.found");
	}

	public Sort defaultSorting(String sortBy, String sortDirection) {
		return sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
	}

	// -----------------US574 end--------------

	@Test
	void changePasswordFirstLogin_WhenUserStateIsNotINIT_Expect_ReturnResponseMessage() {
		Role role = mock(Role.class);
		Location location = mock(Location.class);
		Users users = new Users("SD0001", "duc", "nguyen", "ducnguyen", "123", null, null, true, UserState.ACTIVE, role,
				location, null);
		Optional<Users> usersOptional = Optional.of(users);
		when(userRepository.findByUserName("duc")).thenReturn(usersOptional);
		ResponseMessage expected = new ResponseMessage(HttpStatus.CONFLICT,
				"You don't " + "have to " + "change your password for the first time you log in because your "
						+ "password has already been changed.",
				new Date());
		ResponseMessage actual = userServiceImpl.changePasswordFirstLogin("duc", "123");
		assertThat(actual.getMessage()).isEqualTo(expected.getMessage());
		assertThat(actual.getStatus()).isEqualTo(expected.getStatus());
	}

	@Test
	void changePasswordFirstLogin_WhenNewPasswordEqualNewPassword_Expect_ReturnResponseMessage() {
		Role role = mock(Role.class);
		Location location = mock(Location.class);
		Users users = new Users("SD0001", "duc", "nguyen", "ducnguyen", "123", null, null, true, UserState.INIT, role,
				location, null);
		Optional<Users> usersOptional = Optional.of(users);
		when(userRepository.findByUserName("duc")).thenReturn(usersOptional);
		when(passwordEncoder.matches("123", users.getPassword())).thenReturn(true);
		ResponseMessage expected = new ResponseMessage(HttpStatus.CONFLICT,
				"The new password must be " + "different from the previous password.", new Date());
		ResponseMessage actual = userServiceImpl.changePasswordFirstLogin("duc", "123");
		assertThat(actual.getMessage()).isEqualTo(expected.getMessage());
		assertThat(actual.getStatus()).isEqualTo(expected.getStatus());
	}

//	=================================change password===========================================

	@DisplayName("change password throw password incorrect when user exist")
	@Test
	public void changePassword_shouldReturnExceptionPasswordIncorrect_whenUserIdExist() {
		Users entity = mock(Users.class);
		RequestChangePassDto request = mock(RequestChangePassDto.class);
		when(userRepository.findById(request.getStaffCode())).thenReturn(Optional.of(entity));
		when(passwordEncoder.matches(request.getPassword(), entity.getPassword())).thenReturn(false);
		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			userServiceImpl.changePassword(request);
		});
		assertThat(exception.getMessage()).isEqualTo("Password is incorrect");
	}

	@DisplayName("change password throw same password when user exist")
	@Test
	public void changePassword_shouldReturnExceptionSamePassword_whenUserIdExist() {
		Users entity = mock(Users.class);
		RequestChangePassDto request = mock(RequestChangePassDto.class);
		when(userRepository.findById(request.getStaffCode())).thenReturn(Optional.of(entity));
		when(passwordEncoder.matches(request.getNewPassword(), entity.getPassword())).thenReturn(true);
		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			userServiceImpl.changePassword(request);
		});
		assertThat(exception.getMessage()).isEqualTo("You entered an old password");
	}

	@DisplayName("change password throw not found when user not exist")
	@Test
	public void changePassword_shouldThrowsExceptionNotFound_whenNotFound() {
		RequestChangePassDto requestDto = mock(RequestChangePassDto.class);
		when(userRepository.findById(requestDto.getStaffCode())).thenReturn(Optional.empty());
		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			userServiceImpl.changePassword(requestDto);
		});
		assertThat(exception.getMessage()).isEqualTo("user.not.found.with.staff.code:" + requestDto.getStaffCode());
	}
	
//	=================================#578 disable user===========================================

	@Test
	public void checkExistsAssignment_shouldReturnException_whenUserIdExist() {
		Users entity = mock(Users.class);
		when(userRepository.findById("SD001")).thenReturn(Optional.of(entity));
		when(assignmentRepository.existsByAssignedToOrAssignedBy(entity, entity)).thenReturn(true);
		Exception exception = assertThrows(DateInvalidException.class, () -> {
			userServiceImpl.checkExistsAssignment("SD001");
		});
		assertThat(exception.getMessage()).isEqualTo("exist.valid.assignments");
	}

	@Test
	public void checkExistsAssignment_shouldThrowsExceptionNotFound_whenUserIdNotExist() {
		when(userRepository.findById("SD001")).thenReturn(Optional.empty());
		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			userServiceImpl.checkExistsAssignment("SD001");
		});
		assertThat(exception.getMessage()).isEqualTo("staff.not.found.with.code:SD001");
	}

	@Test
	public void disableStaff_shouldThrowsExceptionNotFound_whenUserIdNotExist() {
		when(userRepository.findById("SD001")).thenReturn(Optional.empty());
		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			userServiceImpl.checkExistsAssignment("SD001");
		});
		assertThat(exception.getMessage()).isEqualTo("staff.not.found.with.code:SD001");
	}

	@Test
	public void disableStaff_shouldReturnResponseDto_whenUserIdExist() {
		
		Users entity = mock(Users.class);
		ResponseUserDTO expected = mock(ResponseUserDTO.class);
		
		when(userRepository.findById("SD001")).thenReturn(Optional.of(entity));
		when(userRepository.save(entity)).thenReturn(entity);
		when(modelMapper.map(entity, ResponseUserDTO.class)).thenReturn(expected);
		
		ResponseUserDTO actual = userServiceImpl.disableStaff("SD001");
		verify(entity).setState(UserState.INACTIVE);
		assertThat(actual).isEqualTo(expected);
	}

}
