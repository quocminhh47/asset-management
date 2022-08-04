package com.nashtech.assetmanagement.service;

import com.nashtech.assetmanagement.dto.response.LocationResponseDTO;

import java.util.List;

public interface LocationService {

    List<LocationResponseDTO> getLocationList();
}
