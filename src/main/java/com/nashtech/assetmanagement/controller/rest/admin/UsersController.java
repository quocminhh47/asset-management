package com.nashtech.assetmanagement.controller.rest.admin;

import com.nashtech.assetmanagement.dto.request.UserRequestDto;
import com.nashtech.assetmanagement.dto.response.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nashtech.assetmanagement.dto.response.ListUsersResponseDto;
import com.nashtech.assetmanagement.dto.response.UserResponseDto;
import com.nashtech.assetmanagement.dto.response.SingleUserResponseDto;
import com.nashtech.assetmanagement.service.UserService;
import com.nashtech.assetmanagement.utils.AppConstants;

import lombok.AllArgsConstructor;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/admin/api/users")
public class UsersController {

	private final UserService userService;

	@PostMapping()
	@ResponseStatus(HttpStatus.CREATED)
	public UserContentResponseDto createNewUser(@RequestBody @Valid UserRequestDto user){
		return userService.createNewUser(user);
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public UserContentResponseDto editUser(@RequestBody @Valid UserRequestDto user, @PathVariable("id") String staffCode){
		return userService.editUser(user,staffCode);
	}

	@GetMapping("/location/{staffCode}")
	public ResponseEntity<LocationResponseDto> getLocationByStaffCode(@PathVariable("staffCode")String id){
		return ResponseEntity.ok(this.userService.getLocationByStaffCode(id));
	}

	@GetMapping("/searching/{location}")
	public ResponseEntity<HashMap> getUserListByStaffCodeOrName(@RequestParam("text")String text, @PathVariable("location")String locationCode){
		HashMap hashMap = new HashMap();
		List<UserContentResponseDto> result = this.userService.getUsersByStaffCodeOrNameAndLocationCode(text,locationCode);
		hashMap.put("list_user",result);
		hashMap.put("total",result.size());
		return ResponseEntity.ok(hashMap);
	}

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ListUsersResponseDto getAllStaffOrderByFirstNameAsc(
            @RequestParam(value = "textPattern", defaultValue = "" ) String searchText,
            @RequestParam("roles") List<String> roles,
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

	@GetMapping("/checking/{staffCode}")
	public void checkExistsByAssigned(@PathVariable("staffCode") String staffCode) {
		userService.checkExistsAssignment(staffCode);
	}

	@DeleteMapping("/{staffCode}")
	public ResponseEntity<UserResponseDto> disableStaff(@PathVariable("staffCode") String staffCode) {
		UserResponseDto dto = userService.disableStaff(staffCode);
		return new ResponseEntity<UserResponseDto>(dto, HttpStatus.OK);
	}
}
