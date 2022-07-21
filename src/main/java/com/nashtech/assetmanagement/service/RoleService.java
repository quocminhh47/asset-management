package com.nashtech.assetmanagement.service;

import com.nashtech.assetmanagement.entities.Role;

public interface RoleService {
    Role getRole(String name);

    Role getRole(Long id);
}
