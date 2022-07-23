package com.nashtech.assetmanagement.controller.rest;

import com.nashtech.assetmanagement.dto.request.RequestFirstLogin;
import com.nashtech.assetmanagement.dto.response.ResponseMessage;
import com.nashtech.assetmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/user/api")
public class UserController {

    UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/first-login")
    public ResponseEntity<?> changePasswordFirstLogin(@Valid @RequestBody RequestFirstLogin requestFirstLogin) {
        ResponseMessage responseMessage=
                userService.changePasswordFirstLogin(requestFirstLogin.getUserName(),
                        requestFirstLogin.getNewPassword());
        return new ResponseEntity<>(responseMessage,responseMessage.getStatus());
    }

}
