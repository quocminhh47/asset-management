package com.nashtech.assetmanagement.controller.rest;

import org.springframework.context.annotation.Role;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @GetMapping("/admin/api/admin")
    public String get1(){
        return "admin";
    }
    @GetMapping("/user/api/user")
    public String get2(){
        return "user";
    }
}
