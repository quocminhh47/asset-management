package com.nashtech.assetmanagement.service.impl;

import com.nashtech.assetmanagement.entities.Role;
import com.nashtech.assetmanagement.exception.ResourceNotFoundException;
import com.nashtech.assetmanagement.repositories.RoleRepository;
import com.nashtech.assetmanagement.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    @Override
    public Role getRole(String name) {
        Role role =
                roleRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Did not find role with name: " + name)
                );
        return role;
    }

    @Override
    public Role getRole(Long id){
        Role role =
                roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Did not find role with id: " + id)
                );
        return role;
    }
}
