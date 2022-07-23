package com.nashtech.assetmanagement.controller.rest;

import com.nashtech.assetmanagement.dto.request.RequestChangePassDto;
import com.nashtech.assetmanagement.dto.request.RequestFirstLogin;
import com.nashtech.assetmanagement.dto.response.ResponseMessage;
import com.nashtech.assetmanagement.dto.response.ResponseUserDTO;
import com.nashtech.assetmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.assetmanagement.dto.request.RequestFirstLogin;
import com.nashtech.assetmanagement.dto.request.UserRequestDto;
import com.nashtech.assetmanagement.dto.response.LocationResponseDTO;
import com.nashtech.assetmanagement.dto.response.ResponseMessage;
import com.nashtech.assetmanagement.service.UserService;
@RequestMapping("/admin/api")
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/create")
    public void createNewUser(@RequestBody @Valid UserRequestDto user){
        this.userService.createNewUser(user);
    }

    @PutMapping("/edit/{id}")
    public void editUser(@RequestBody @Valid UserRequestDto user, @PathVariable("id") String staffCode){
        this.userService.editUser(user,staffCode);
    }

    @GetMapping("/location/{staffCode}")
    public ResponseEntity<LocationResponseDTO> getLocationByStaffCode(@PathVariable("staffCode")String id){
        return ResponseEntity.ok(this.userService.getLocationByStaffCode(id));
    }
    @GetMapping("/admin/api/admin")
    public String get1(){
        return "admin";
    }

    @PostMapping("/first-login")
    public ResponseEntity<?> changePasswordFirstLogin(@Valid @RequestBody RequestFirstLogin requestFirstLogin) {
        ResponseMessage responseMessage=
                userService.changePasswordFirstLogin(requestFirstLogin.getUserName(),
                        requestFirstLogin.getNewPassword());
        return new ResponseEntity<>(responseMessage,responseMessage.getStatus());
    }
}
