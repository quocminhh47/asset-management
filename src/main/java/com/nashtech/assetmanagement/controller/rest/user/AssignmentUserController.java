package com.nashtech.assetmanagement.controller.rest.user;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Assignment Resources",
		description = "Provide the all assignment information of the current user")
@RestController
@AllArgsConstructor
@RequestMapping("/users/api/assignments")
public class AssignmentUserController {

	private AssignmentService assignmentService;


	@Operation(summary = "Get all assets belong to the current user",
			description = "This return all assets which belonged to the current user at that moment")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OK - Successfully retrieved"),
			@ApiResponse(responseCode = "400",
					description = "Bad Request - The request is invalid",
					content = {@Content(examples = {@ExampleObject()})}),
			@ApiResponse(responseCode = "401", description = "UNAUTHORIZED - The request is unauthorized"),
			@ApiResponse(responseCode = "403", description = "FORBIDDEN - You don’t have permission to access"),
			@ApiResponse(responseCode = "404", description = "NOT FOUND - The asset resource of current user is not found"),
			@ApiResponse(responseCode = "409",
					description = "CONFLICT - The asset cannot deleted cause it's already belonged to an existing assignment"),
			@ApiResponse(responseCode = "500",
					description = "Internal Error - There were some error while processing in server",
					content = {@Content(examples = {@ExampleObject()})})
	})
	@GetMapping("/{userId}")
	public ResponseEntity<List<AssignmentResponseDto>> getListAsset(@PathVariable("userId") String userId,
			@RequestParam(required = false, defaultValue = "", value = "sortBy") String sortBy,
			@RequestParam(required = false, defaultValue = "", value = "sortDirection") String sortDirection) {
		List<AssignmentResponseDto> result = assignmentService.getListAssignmentByUser(userId, sortBy, sortDirection);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

}
