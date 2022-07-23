package com.nashtech.assetmanagement.mapper;

import com.nashtech.assetmanagement.dto.response.ResponseUserDTO;
import com.nashtech.assetmanagement.dto.response.SingleUserResponse;
import com.nashtech.assetmanagement.entities.Users;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserMapper {

    private final ModelMapper mapper;

    public SingleUserResponse convertUserEntityToSingleUserResponse(Users user) {
        return mapper.map(user, SingleUserResponse.class);
    }
    public ResponseUserDTO userToResponseUser(Users users) {
        return mapper.map(users, ResponseUserDTO.class);
    }

}
