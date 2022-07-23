package com.nashtech.assetmanagement.mapper;

import com.nashtech.assetmanagement.dto.request.UserRequestDto;
import com.nashtech.assetmanagement.dto.response.ResponseUserDTO;
import com.nashtech.assetmanagement.dto.response.SingleUserResponse;
import com.nashtech.assetmanagement.entities.Location;
import com.nashtech.assetmanagement.entities.Role;
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

    public static Users MapToUser(UserRequestDto dto, Role role, Location location){
        Users newUser = new Users();
        newUser.setFirstName(dto.getFirstName());
        newUser.setLastName(dto.getLastName());
        newUser.setBirthDate(dto.getBirthDate());
        newUser.setJoinedDate(dto.getJoinedDate());
        newUser.setGender(dto.getGender());
        newUser.setRole(role);
        newUser.setLocation(location);
        return newUser;
    }

    public void requestDtoToUser(Users users, UserRequestDto user,Role role) {
        users.setBirthDate(user.getBirthDate());
        users.setJoinedDate(user.getJoinedDate());
        users.setGender(user.getGender());
        users.setRole(role);
    }
}
