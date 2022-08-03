package com.nashtech.assetmanagement.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.nashtech.assetmanagement.dto.request.RequestAssignmentDTO;
import com.nashtech.assetmanagement.dto.response.AssignmentDto;
import com.nashtech.assetmanagement.dto.response.ListAssignmentResponse;
import com.nashtech.assetmanagement.entities.Asset;
import com.nashtech.assetmanagement.entities.Assignment;
import com.nashtech.assetmanagement.entities.Users;
import com.nashtech.assetmanagement.enums.AssetState;
import com.nashtech.assetmanagement.exception.DateInvalidException;
import com.nashtech.assetmanagement.exception.ResourceNotFoundException;
import com.nashtech.assetmanagement.mapper.AssignmentContent;
import com.nashtech.assetmanagement.mapper.AssignmentMapper;
import com.nashtech.assetmanagement.repositories.AssetRepository;
import com.nashtech.assetmanagement.repositories.AssignmentRepository;
import com.nashtech.assetmanagement.repositories.UserRepository;
import com.nashtech.assetmanagement.utils.StateConverter;

class AssignmentServiceImplTest {

	AssignmentRepository assignmentRepository;
	AssignmentContent assignmentContent;
	UserRepository userRepository;
	AssetRepository assetRepository;
	AssignmentMapper assignmentMapper;
	AssignmentServiceImpl assignmentServiceImpl;
	List<String> states;

	@BeforeEach
	void setUp() {
		assignmentRepository = mock(AssignmentRepository.class);
		assignmentContent = mock(AssignmentContent.class);
		userRepository = mock(UserRepository.class);
		assetRepository = mock(AssetRepository.class);
		assignmentMapper = mock(AssignmentMapper.class);
		assignmentServiceImpl = new AssignmentServiceImpl(assignmentRepository, assignmentContent, userRepository,
				assetRepository, assignmentMapper);

		String[] stateArray = { "accepted", "declined" };

		states = Arrays.asList(stateArray);

	}

	@DisplayName("Given valid date and valid condition then return list assignment response positive case")
	@Test
	void givenValidCondition_whenGetAssignmentsByCondition_thenReturnListAssignmentResponse() {
		// given
		ListAssignmentResponse expectedResponse = mock(ListAssignmentResponse.class);

		List<String> assignedState = states.stream().map(StateConverter::getAssignmentState)
				.collect(Collectors.toList());

		assertThat(assignedState.size()).isEqualTo(2);
		assertThat(assignedState.get(0)).isEqualTo("Accepted");
		assertThat(assignedState.get(1)).isEqualTo("Declined");

		Pageable pageable = PageRequest.of(0, 1);
		Page<Assignment> assignmentPage = mock(Page.class);
		String assignedDateStr = "2022-06-06";
		Date assignedDate = Date.valueOf(assignedDateStr); // pattern: yyyy/mm/dd
		Date dateNow = new Date(new java.util.Date().getTime());
		assertThat(assignedDate.before(dateNow)).isTrue();
		when(assignmentRepository.getAssignmentByConditions("SD".toLowerCase(), assignedState, assignedDate, pageable))
				.thenReturn(assignmentPage);
		when(assignmentContent.getAssignmentResponse(assignmentPage)).thenReturn(expectedResponse);

		// when
		ListAssignmentResponse actualResponse = assignmentServiceImpl.getAssignmentsByCondition(0, 1, "SD", states,
				assignedDateStr);
		// then
		assertThat(actualResponse).isEqualTo(expectedResponse);
	}

	@Test
	void givenWithoutDateCondition_whenGetAssignmentsByCondition_thenReturnListAssignmentResponse() {
		// given
		ListAssignmentResponse expectedResponse = mock(ListAssignmentResponse.class);

		List<String> assignedState = states.stream().map(StateConverter::getAssignmentState)
				.collect(Collectors.toList());

		assertThat(assignedState.size()).isEqualTo(2);
		assertThat(assignedState.get(0)).isEqualTo("Accepted");
		assertThat(assignedState.get(1)).isEqualTo("Declined");

		String assignedDateStr = " ";

		Pageable pageable = PageRequest.of(0, 1);
		Page<Assignment> assignmentPage = mock(Page.class);
		when(assignmentRepository.getAssignmentWithoutAssignedDate("SD".toLowerCase(), assignedState, pageable))
				.thenReturn(assignmentPage);
		when(assignmentContent.getAssignmentResponse(assignmentPage)).thenReturn(expectedResponse);

		// when
		ListAssignmentResponse actualResponse = assignmentServiceImpl.getAssignmentsByCondition(0, 1, "SD", states,
				assignedDateStr);
		// then
		assertThat(actualResponse).isEqualTo(expectedResponse);

	}

	@DisplayName("Given invalid date when get list assignments then throw exception- negative case")
	@Test
	void givenInvalidDate_whenGetAssignmentsByCondition_thenThrowsException() {
		// given
		List<String> assignedState = states.stream().map(StateConverter::getAssignmentState)
				.collect(Collectors.toList());
		String dateInFuture = "2023-06-06";
		String invalidFormatDate = "20230-06-06";
		// when
		DateInvalidException dateFutureExcep = Assertions.assertThrows(DateInvalidException.class,
				() -> assignmentServiceImpl.getAssignmentsByCondition(0, 1, "", assignedState, dateInFuture));

		IllegalArgumentException invalidDateFormatExcep = Assertions.assertThrows(IllegalArgumentException.class,
				() -> assignmentServiceImpl.getAssignmentsByCondition(0, 1, "", assignedState, invalidFormatDate));
		// then
		Date dateNow = new Date(new java.util.Date().getTime());
		assertThat(dateFutureExcep.getMessage())
				.isEqualTo("Date.is.must.before.today:" + dateNow.toString().replaceAll(" ", "."));
		assertThat(invalidDateFormatExcep.getMessage()).isEqualTo("Date.format.is.not.valid");
	}

	// US584 Create New Assignment
	@Test
	void createNewAssignment_ShouldReturnResponseAssignmentDto_WhenRequestValid() {
		RequestAssignmentDTO request = mock(RequestAssignmentDTO.class);
		when(request.getAssignedTo()).thenReturn("assignTo");
		when(request.getAssignedBy()).thenReturn("assignBy");
		when(request.getAssetCode()).thenReturn("assetCode");
		Asset asset = mock(Asset.class);
		Users assignBy = mock(Users.class);
		Users assignTo = mock(Users.class);
		Assignment assignment = mock(Assignment.class);
		AssignmentDto response = mock(AssignmentDto.class);
		when(assetRepository.findById("assetCode")).thenReturn(Optional.of(asset));
		when(userRepository.findById("assignBy")).thenReturn(Optional.of(assignBy));
		when(userRepository.findById("assignTo")).thenReturn(Optional.of(assignTo));
		when(assignmentMapper.MapRequestAssignmentToAssignment(request)).thenReturn(assignment);
		when(assignmentMapper.MapAssignmentToResponseDto(assignment)).thenReturn(response);
		AssignmentDto result = assignmentServiceImpl.createNewAssignment(request);
		verify(asset).setState(AssetState.ASSIGNED);
		verify(assignment).setAssignedTo(assignTo);
		verify(assignment).setAssignedBy(assignBy);
		verify(assignment).setAsset(asset);
		verify(assignmentRepository).save(assignment);
		assertThat(result).isEqualTo(response);
	}

	@Test
	void createNewAssignment_ShouldThrowResourceNotFoundEx_WhenAssetCodeNotExist() {
		Asset asset = mock(Asset.class);
		RequestAssignmentDTO request = mock(RequestAssignmentDTO.class);
		when(assetRepository.findById("assetCode")).thenReturn(Optional.empty());
		ResourceNotFoundException e = Assertions.assertThrows(ResourceNotFoundException.class,
				() -> assignmentServiceImpl.createNewAssignment(request));
		assertThat(e.getMessage()).isEqualTo("AssetCode not found");
	}

	@Test
	void createNewAssignment_ShouldThrowResourceNotFoundEx_WhenUserAssignToNotExist() {
		Asset asset = mock(Asset.class);
		RequestAssignmentDTO request = mock(RequestAssignmentDTO.class);
		when(request.getAssetCode()).thenReturn("assetCode");
		when(request.getAssignedTo()).thenReturn("assignTo");
		when(assetRepository.findById("assetCode")).thenReturn(Optional.of(asset));
		when(userRepository.findById("assignTo")).thenReturn(Optional.empty());
		ResourceNotFoundException e = Assertions.assertThrows(ResourceNotFoundException.class,
				() -> assignmentServiceImpl.createNewAssignment(request));
		assertThat(e.getMessage()).isEqualTo("Assign to User not found");
	}

	@Test
	void createNewAssignment_ShouldThrowResourceNotFoundEx_WhenUserAssignByNotExist() {
		Asset asset = mock(Asset.class);
		Users assignTo = mock(Users.class);
		RequestAssignmentDTO request = mock(RequestAssignmentDTO.class);
		when(request.getAssetCode()).thenReturn("assetCode");
		when(request.getAssignedTo()).thenReturn("assignTo");
		when(request.getAssignedBy()).thenReturn("assignBy");
		when(assetRepository.findById("assetCode")).thenReturn(Optional.of(asset));
		when(userRepository.findById("assignTo")).thenReturn(Optional.of(assignTo));
		when(userRepository.findById("assignBy")).thenReturn(Optional.empty());
		ResourceNotFoundException e = Assertions.assertThrows(ResourceNotFoundException.class,
				() -> assignmentServiceImpl.createNewAssignment(request));
		assertThat(e.getMessage()).isEqualTo("Assign by User not found");
	}

	@Test
	void getListAssignmentByAsset_ShouldReturnListAssignmentDto_WhenAssetIdExist() {
		Asset entity = mock(Asset.class);
		List<Assignment> listEntity = mock(List.class);
		List<AssignmentDto> expected = mock(List.class);
		when(assetRepository.findById("Laptop001")).thenReturn(Optional.of(entity));
		when(assignmentRepository.findByAsset(entity)).thenReturn(listEntity);
		when(assignmentMapper.mapperListAssignment(listEntity)).thenReturn(expected);

		List<AssignmentDto> actual = assignmentServiceImpl.getListAssignmentByAssetCode("Laptop001");
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void getListAssignmentByAsset_ShouldReturnListAssignmentDto_WhenAssetIdNotExist() {
		when(assetRepository.findById("Laptop001")).thenReturn(Optional.empty());
		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			assignmentServiceImpl.getListAssignmentByAssetCode("Laptop001");
		});
		assertThat(exception.getMessage()).isEqualTo("asset.not.found.with.code:Laptop001");
	}

	@Test
	void getListAssignmentByUser_whenThrowResourceNotFound_whenUserIdNotExist() {
		Users user = mock(Users.class);
		when(userRepository.findById("SD001")).thenReturn(Optional.empty());
		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			assignmentServiceImpl.getListAssignmentByUser("SD001", "code", "ASC");
		});
		assertThat(exception.getMessage()).isEqualTo("user.not.found.with.code:SD001");
	}

	@Test
	void getListAssignmentByUser_whenReturnListAssignmentDto_whenUserIdExist() {
		Users user = mock(Users.class);
		Page<Assignment> pageAssignment = mock(Page.class);
		List<Assignment> listAssignment = mock(List.class);
		List<AssignmentDto> expectList = mock(List.class);
		
		when(userRepository.findById("SD001")).thenReturn(Optional.of(user));
		when(assignmentRepository.getListAssignmentByUser(eq("SD001"), Mockito.any(Pageable.class))).thenReturn(pageAssignment);
		when(pageAssignment.getContent()).thenReturn(listAssignment);
		when(assignmentMapper.mapperListAssignment(listAssignment)).thenReturn(expectList);
		
	
		List<AssignmentDto> actual = assignmentServiceImpl.getListAssignmentByUser("SD001", "code", "ASC");
	
		ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
		verify(assignmentRepository).getListAssignmentByUser(eq("SD001"), captor.capture());
		Pageable pageable = captor.getValue();
		
		assertThat(pageable.getPageSize()).isEqualTo(20);
		assertThat(pageable.getPageNumber()).isEqualTo(0);
		assertThat(pageable.getSort().ascending()).isEqualTo(Sort.by("code"));
		assertThat(actual).isEqualTo(expectList);
	}

}