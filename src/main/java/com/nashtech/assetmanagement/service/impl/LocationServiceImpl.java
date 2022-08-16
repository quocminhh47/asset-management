package com.nashtech.assetmanagement.service.impl;

import com.nashtech.assetmanagement.dto.response.LocationResponseDto;
import com.nashtech.assetmanagement.entities.Location;
import com.nashtech.assetmanagement.exception.ResourceNotFoundException;
import com.nashtech.assetmanagement.mapper.LocationMapper;
import com.nashtech.assetmanagement.repositories.LocationRepository;
import com.nashtech.assetmanagement.service.LocationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    public LocationServiceImpl(LocationRepository locationRepository, LocationMapper locationMapper) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
    }

    @Override
    public List<LocationResponseDto> getLocationList() {
        List<Location> locations = locationRepository.findAll();
        if (locations.isEmpty()) {
            throw new ResourceNotFoundException("Location list not found");
        }
        return locationMapper.locationListToLocationResponseDtoList(locations);
    }
}
