package com.nashtech.assetmanagement.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.nashtech.assetmanagement.dto.request.RequestChangePassDto;
import com.nashtech.assetmanagement.dto.response.ResponseUserDTO;
import com.nashtech.assetmanagement.entities.Users;
import com.nashtech.assetmanagement.exception.ResourceNotFoundException;
import com.nashtech.assetmanagement.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceImplTest {

	@Mock
	private UserRepository userRepository;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@Mock
	private ModelMapper modelMapper;
	
	@InjectMocks
	private UserServiceImpl userServiceImpl;
	
	@Test
	public void changePassword_shouldReturnResponseUserDTO_whenUserIdExist() {
		
		Users entity = mock(Users.class);
		RequestChangePassDto requestDto = mock(RequestChangePassDto.class);
		ResponseUserDTO expected = mock(ResponseUserDTO.class);
		
		when(userRepository.findById(requestDto.getStaffCode())).thenReturn(Optional.of(entity));
		when(userRepository.save(entity)).thenReturn(entity);
		when(modelMapper.map(entity, ResponseUserDTO.class)).thenReturn(expected);
		
		ResponseUserDTO actual = userServiceImpl.changePassword(requestDto);
		
		verify(entity).setPassword(passwordEncoder.encode(requestDto.getNewPassword()));
		
		assertThat(actual).isEqualTo(expected);
	}
	
	@Test
	public void changePassword_shouldThrowsExceptionNotFound_whenNotFound() {
		RequestChangePassDto requestDto = mock(RequestChangePassDto.class);
		
		Mockito.when(userRepository.findById(requestDto.getStaffCode())).thenReturn(Optional.ofNullable(null));
		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			userServiceImpl.changePassword(requestDto);
		});
		
		assertThat(exception.getMessage()).isEqualTo("user.not.found.with.staff.code:SD001");
	}
	
}
