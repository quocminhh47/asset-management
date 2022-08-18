package com.nashtech.assetmanagement.mapper;

import com.nashtech.assetmanagement.dto.request.UserRequestDto;
import com.nashtech.assetmanagement.dto.response.ListSearchingUserResponseDto;
import com.nashtech.assetmanagement.dto.response.SingleUserResponseDto;
import com.nashtech.assetmanagement.dto.response.UserContentResponseDto;
import com.nashtech.assetmanagement.dto.response.UserResponseDto;
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

    public SingleUserResponseDto convertUserEntityToSingleUserResponse(Users user) {
        return mapper.map(user, SingleUserResponseDto.class);
    }

    public UserResponseDto userToResponseUser(Users users) {
        return mapper.map(users, UserResponseDto.class);
    }

    public Users mapToUser(UserRequestDto dto, Role role, Location location) {
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

    public void requestDtoToUser(Users users, UserRequestDto user, Role role) {
        users.setBirthDate(user.getBirthDate());
        users.setJoinedDate(user.getJoinedDate());
        users.setGender(user.getGender());
        users.setRole(role);
    }

    public ListSearchingUserResponseDto mapListUserToListUserDto(List<Users> usersList) {
        List<UserContentResponseDto> responseList = usersList.stream().map(users -> mapper.map(users, UserContentResponseDto.class)).collect(Collectors.toList());
        return new ListSearchingUserResponseDto(responseList.size(),responseList);
    }

}
