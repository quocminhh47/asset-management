package com.nashtech.assetmanagement.controller.rest.user;

import com.nashtech.assetmanagement.dto.request.ChangePassRequestDto;
import com.nashtech.assetmanagement.dto.request.FirstLoginRequestDto;
import com.nashtech.assetmanagement.dto.request.UserRequestDto;
import com.nashtech.assetmanagement.dto.response.LocationResponseDto;
import com.nashtech.assetmanagement.dto.response.MessageResponse;
import com.nashtech.assetmanagement.dto.response.UserResponseDto;
import com.nashtech.assetmanagement.dto.response.UserContentResponseDto;
import com.nashtech.assetmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RequestMapping("/users/api")
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PutMapping("/first-login")
    public ResponseEntity<?> changePasswordFirstLogin(@Valid @RequestBody FirstLoginRequestDto requestFirstLogin) {
        MessageResponse responseMessage=
                userService.changePasswordFirstLogin(requestFirstLogin.getUserName(),
                        requestFirstLogin.getNewPassword());

        return new ResponseEntity<>(responseMessage,responseMessage.getStatus());
    }
    @PutMapping("/password")
    public ResponseEntity<UserResponseDto> changePassword(@Valid @RequestBody ChangePassRequestDto requestChangePassDto){
        return ResponseEntity.ok(userService.changePassword(requestChangePassDto));
    }
}
