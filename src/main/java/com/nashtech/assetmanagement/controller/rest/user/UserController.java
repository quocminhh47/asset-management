package com.nashtech.assetmanagement.controller.rest.user;

import com.nashtech.assetmanagement.dto.request.RequestChangePassDto;
import com.nashtech.assetmanagement.dto.response.ResponseUserDTO;
import com.nashtech.assetmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController(value = "userControllerOfUser")
@RequestMapping("/user/api")
public class UserController {
	
    @Autowired
     private UserService userService;
    
    @PostMapping("/change-password")
    public ResponseEntity<ResponseUserDTO> changePassword(@RequestBody RequestChangePassDto requestChangePassDto){
    	return ResponseEntity.ok(userService.changePassword(requestChangePassDto));
    }
}
