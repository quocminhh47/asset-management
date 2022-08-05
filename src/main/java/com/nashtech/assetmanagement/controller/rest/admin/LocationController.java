package com.nashtech.assetmanagement.controller.rest.admin;

import com.nashtech.assetmanagement.dto.response.LocationResponseDto;
import com.nashtech.assetmanagement.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/api/locations")
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("")
    public ResponseEntity<List<LocationResponseDto>> getLocationList() {
        return ResponseEntity.ok(this.locationService.getLocationList());
    }
}
