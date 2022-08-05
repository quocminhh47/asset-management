package com.nashtech.assetmanagement.controller.rest.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.assetmanagement.dto.response.AssignmentResponseDto;
import com.nashtech.assetmanagement.service.AssignmentService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/user/api/assignment")
public class AssignmentUserController {

	@Autowired
	private AssignmentService assignmentService;

	@GetMapping("/{userId}")
	public ResponseEntity<List<AssignmentResponseDto>> getListAsset(@PathVariable("userId") String userId,
			@RequestParam(required = false, defaultValue = "", value = "sortBy") String sortBy,
			@RequestParam(required = false, defaultValue = "", value = "sortDirection") String sortDirection) {
		List<AssignmentResponseDto> result = assignmentService.getListAssignmentByUser(userId, sortBy, sortDirection);
		return new ResponseEntity<List<AssignmentResponseDto>>(result, HttpStatus.OK);
	}

}
