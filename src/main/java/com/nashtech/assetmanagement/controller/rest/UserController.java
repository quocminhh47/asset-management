package com.nashtech.assetmanagement.controller.rest;

import com.nashtech.assetmanagement.dto.request.UserRequestDto;
import com.nashtech.assetmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
@RequestMapping("")
@RestController
public class UserController {
    UserService userService;

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
}
