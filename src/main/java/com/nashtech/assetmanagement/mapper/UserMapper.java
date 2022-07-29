package com.nashtech.assetmanagement.mapper;

import com.nashtech.assetmanagement.dto.request.UserRequestDto;
import com.nashtech.assetmanagement.dto.response.ResponseUserDTO;
import com.nashtech.assetmanagement.dto.response.SingleUserResponse;
import com.nashtech.assetmanagement.dto.response.UserDto;
import com.nashtech.assetmanagement.entities.Location;
import com.nashtech.assetmanagement.entities.Role;
import com.nashtech.assetmanagement.entities.Users;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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

    public Users MapToUser(UserRequestDto dto, Role role, Location location){
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

    public List<UserDto> mapListUserToListUserDto(List<Users> usersList){
        List<UserDto> result = usersList.stream().map(users -> mapper.map(users,UserDto.class)).collect(Collectors.toList());
        return result;
    }

}
