package com.nashtech.assetmanagement.controller.rest.admin;

import com.nashtech.assetmanagement.dto.response.ListUsersResponse;
import com.nashtech.assetmanagement.dto.response.SingleUserResponse;
import com.nashtech.assetmanagement.service.UserService;
import com.nashtech.assetmanagement.utils.AppConstants;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/admin/api/users")
public class UsersManagementController {

    private final UserService userService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ListUsersResponse getAllStaffOrderByFirstNameAsc(
            @RequestParam(
                    value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false)
                    int pageNo,
            @RequestParam(
                    value = "pageSize", defaultValue = "5", required = false)
//                    value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false)
                    int pageSize,
            @RequestParam(
                    value = "sortBy", defaultValue = "firstName", required = false)
                    String sortBy,
            @RequestParam(
                    value = "sortDirection", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false)
                    String sortDirection
    ) {
        return userService.getAllUserOrderByFirstNameAsc(pageNo, pageSize, sortBy,sortDirection);
    }

    @GetMapping("/searching")
    @ResponseStatus(HttpStatus.OK)
    public ListUsersResponse getAllStaffOrderByFirstNameAsc(
            @RequestParam("textPattern") String searchText,
            @RequestParam(
                    value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false)
                    int pageNo,
            @RequestParam(
                    value = "pageSize", defaultValue = "5", required = false)
//                    value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false)
                    int pageSize,
            @RequestParam(
                    value = "sortBy", defaultValue = "first_name", required = false)
                    String sortBy,
            @RequestParam(
                    value = "sortDirection", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false)
                    String sortDirection
    ) {
        return userService.getAllUsersBySearchingStaffCodeOrName(pageNo, pageSize, sortBy,sortDirection, searchText);
    }


    @GetMapping("/filter/{role}")
    @ResponseStatus(HttpStatus.OK)
    public ListUsersResponse getAllStaffOrderByRole(
            @PathVariable("role") String roleName,
            @RequestParam(
                    value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false)
                    int pageNo,
            @RequestParam(
                    value = "pageSize", defaultValue = "5", required = false)
//                    value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false)
                    int pageSize,
            @RequestParam(
                    value = "sortBy", defaultValue = "firstName", required = false)
                    String sortBy,
            @RequestParam(
                    value = "sortDirection", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false)
                    String sortDirection
    ) {
        return userService.getAllUsersByRole(pageNo, pageSize, sortBy,sortDirection, roleName);
    }

    @GetMapping("/{staffCode}")
    @ResponseStatus(HttpStatus.OK)
    public SingleUserResponse getUserDetailInfo(@PathVariable("staffCode") String staffCode ) {
        return userService.getUserDetailInfo(staffCode);
    }
}
