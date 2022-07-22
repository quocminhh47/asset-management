package com.nashtech.assetmanagement.controller.rest;

import org.springframework.context.annotation.Role;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
public class UserController {
    @GetMapping("/user/1")
    public String get1(){
        return "user";
    }
    @GetMapping("/admin/2")
    public String get2(){
        return "admin";
    }
}
