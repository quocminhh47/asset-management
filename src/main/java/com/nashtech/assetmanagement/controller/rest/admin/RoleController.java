package com.nashtech.assetmanagement.controller.rest.admin;

import com.nashtech.assetmanagement.dto.response.RoleResponseDto;
import com.nashtech.assetmanagement.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/api/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }


    @GetMapping("")
    public ResponseEntity<List<RoleResponseDto>> getLocationList() {
        return ResponseEntity.ok(this.roleService.getRoleList());
    }
}