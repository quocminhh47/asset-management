package com.nashtech.assetmanagement.mapper;

import com.nashtech.assetmanagement.dto.response.LocationResponseDTO;
import com.nashtech.assetmanagement.dto.response.ResponseRoleDto;
import com.nashtech.assetmanagement.entities.Location;
import com.nashtech.assetmanagement.entities.Role;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class RoleMapper {

    ModelMapper modelMapper;


    public List<ResponseRoleDto> roleListToResponseRoleDtoList (List<Role> roles){
        List<ResponseRoleDto> responseRoleDtoList = roles.stream()
                .map(role -> modelMapper.map(role,ResponseRoleDto.class)).collect(Collectors.toList());
        return  responseRoleDtoList;
    }

}