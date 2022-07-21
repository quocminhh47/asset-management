package com.nashtech.assetmanagement.mapper;

import com.nashtech.assetmanagement.dto.response.ResponseUserDTO;
import com.nashtech.assetmanagement.entities.Users;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    @Autowired
    ModelMapper modelMapper;

    public ResponseUserDTO userToResponseUser(Users users){
        return modelMapper.map(users, ResponseUserDTO.class);
    }

}
