package com.nashtech.assetmanagement.controller.rest.admin;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.assetmanagement.dto.response.ListRequestReturningResponseDto;
import com.nashtech.assetmanagement.enums.RequestReturningState;
import com.nashtech.assetmanagement.service.RequestReturningService;

@RestController
@RequestMapping("/admin/api/requests")
public class RequestReturningController {
	
	@Autowired
	private RequestReturningService requestReturningService;

	@GetMapping
    public ResponseEntity<ListRequestReturningResponseDto> getListRequestReturning(
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "size") Integer size,
            @RequestParam(required = false, defaultValue = "", value = "keyword") String keyword,
            @RequestParam(required = false, defaultValue = "", value = "sortBy") String sortBy,
            @RequestParam(required = false, defaultValue = "", value = "sortDirection") String sortDirection,
            @RequestParam(required = false, defaultValue = "", value = "returnDate") Date returnDate,
            @RequestParam(required = false, defaultValue = "", value = "states") List<String> states) {
		ListRequestReturningResponseDto result = requestReturningService.getListRequestReturning(states, returnDate, keyword, sortBy, sortDirection, page, size);
        return new ResponseEntity<ListRequestReturningResponseDto>(result, HttpStatus.OK);
    }
	
	@GetMapping("/states")
    public ResponseEntity<HashMap<String, String>> getRequestReturningState() {
		HashMap<String, String> hashMap = RequestReturningState.getRequestReturningState();
		return new ResponseEntity<>(hashMap, HttpStatus.OK);
    }
}
