package com.nashtech.assetmanagement.controller.rest.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.assetmanagement.dto.response.ListUsersResponseDto;
import com.nashtech.assetmanagement.dto.response.UserResponseDto;
import com.nashtech.assetmanagement.dto.response.SingleUserResponseDto;
import com.nashtech.assetmanagement.service.UserService;
import com.nashtech.assetmanagement.utils.AppConstants;

import lombok.AllArgsConstructor;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/admin/api/users")
public class UsersController {

	private final UserService userService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ListUsersResponseDto getAllStaffOrderByFirstNameAsc(
            @RequestParam(value = "textPattern", defaultValue = "" ) String searchText,
            @RequestParam("role") List<String> roles,
            @RequestParam(
                    value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false)
                    int pageNo,
            @RequestParam(
                    value = "pageSize", defaultValue = "20", required = false)
                    int pageSize,
            @RequestParam(
                    value = "sortBy", defaultValue = "firstName", required = false)
                    String sortBy,
            @RequestParam(
                    value = "sortDirection", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false)
                    String sortDirection
    ) {
        return userService.getAllUsersBySearchingStaffCodeOrNameOrRole(pageNo, pageSize, sortBy,sortDirection, searchText, roles);
    }

	@GetMapping("/{staffCode}")
	@ResponseStatus(HttpStatus.OK)
	public SingleUserResponseDto getUserDetailInfo(@PathVariable("staffCode") String staffCode) {
		return userService.getUserDetailInfo(staffCode);
	}

	@GetMapping("/check/{staffCode}")
	public void checkExistsByAssigned(@PathVariable("staffCode") String staffCode) {
		userService.checkExistsAssignment(staffCode);
	}

	@GetMapping("/disable/{staffCode}")
	public ResponseEntity<UserResponseDto> disableStaff(@PathVariable("staffCode") String staffCode) {
		UserResponseDto dto = userService.disableStaff(staffCode);
		return new ResponseEntity<UserResponseDto>(dto, HttpStatus.OK);
	}
}