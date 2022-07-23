package com.nashtech.assetmanagement.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.assetmanagement.dto.request.RequestChangePassDto;
import com.nashtech.assetmanagement.dto.response.ResponseUserDTO;
import com.nashtech.assetmanagement.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
    @GetMapping("/user/1")
    public String get1(){
        return "user";
    }
    @GetMapping("/admin/2")
    public String get2(){
        return "admin";
    }
    
    @PostMapping("/users/change-password")
    public ResponseEntity<ResponseUserDTO> changePassword(@RequestBody RequestChangePassDto requestChangePassDto){
    	return ResponseEntity.ok(userService.changePassword(requestChangePassDto));
    }
}
