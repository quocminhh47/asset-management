package com.nashtech.assetmanagement.mapper;

import com.nashtech.assetmanagement.dto.response.LocationResponseDTO;
import com.nashtech.assetmanagement.dto.response.ResponseRoleDto;
import com.nashtech.assetmanagement.entities.Location;
import com.nashtech.assetmanagement.entities.Role;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LocationMapper {
    @Autowired
    ModelMapper modelMapper;
    public LocationResponseDTO locationToLocationDTO(Location location){
        return modelMapper.map(location, LocationResponseDTO.class);
    }
    public List<LocationResponseDTO> locationListToLocationResponseDtoList (List<Location> locations){
        List<LocationResponseDTO> locationResponseDTOList = locations.stream()
                .map(location -> modelMapper.map(location,LocationResponseDTO.class)).collect(Collectors.toList());
        return  locationResponseDTOList;
    }
}
