package com.nashtech.assetmanagement.mapper;

import com.nashtech.assetmanagement.dto.response.LocationResponseDTO;
import com.nashtech.assetmanagement.dto.response.ResponseUserDTO;
import com.nashtech.assetmanagement.entities.Location;
import com.nashtech.assetmanagement.entities.Users;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper {
    @Autowired
    ModelMapper modelMapper;
    public LocationResponseDTO locationToLocationDTO(Location location){
        return modelMapper.map(location, LocationResponseDTO.class);
    }

}
