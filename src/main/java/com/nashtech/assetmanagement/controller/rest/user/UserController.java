package com.nashtech.assetmanagement.controller.rest.user;

import com.nashtech.assetmanagement.dto.request.RequestChangePassDto;
import com.nashtech.assetmanagement.dto.request.RequestFirstLogin;
import com.nashtech.assetmanagement.dto.request.UserRequestDto;
import com.nashtech.assetmanagement.dto.response.LocationResponseDTO;
import com.nashtech.assetmanagement.dto.response.ResponseMessage;
import com.nashtech.assetmanagement.dto.response.ResponseUserDTO;
import com.nashtech.assetmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("")
@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/admin/api/create")
    public void createNewUser(@RequestBody @Valid UserRequestDto user){
        this.userService.createNewUser(user);
    }

    @PutMapping("/admin/api/edit/{id}")
    public void editUser(@RequestBody @Valid UserRequestDto user, @PathVariable("id") String staffCode){
        this.userService.editUser(user,staffCode);
    }

    @GetMapping("/admin/api/location/{staffCode}")
    public ResponseEntity<LocationResponseDTO> getLocationByStaffCode(@PathVariable("staffCode")String id){
        return ResponseEntity.ok(this.userService.getLocationByStaffCode(id));
    }

    @PostMapping("/user/api/first-login")
    public ResponseEntity<?> changePasswordFirstLogin(@Valid @RequestBody RequestFirstLogin requestFirstLogin) {
        ResponseMessage responseMessage=
                userService.changePasswordFirstLogin(requestFirstLogin.getUserName(),
                        requestFirstLogin.getNewPassword());

        return new ResponseEntity<>(responseMessage,responseMessage.getStatus());
    }
    @PostMapping("/user/api/change-password")
    public ResponseEntity<ResponseUserDTO> changePassword(@RequestBody RequestChangePassDto requestChangePassDto){
        return ResponseEntity.ok(userService.changePassword(requestChangePassDto));
    }
}
