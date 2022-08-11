package com.nashtech.assetmanagement.controller.rest.admin;

import java.util.HashMap;
import java.util.List;

import com.nashtech.assetmanagement.dto.request.ReturningRequestDto;
import com.nashtech.assetmanagement.dto.response.RequestReturningResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nashtech.assetmanagement.dto.request.RequestReturningRequestGetListDto;
import com.nashtech.assetmanagement.dto.response.ListRequestReturningResponseDto;
import com.nashtech.assetmanagement.enums.RequestReturningState;
import com.nashtech.assetmanagement.service.RequestReturningService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/api/requests")
@AllArgsConstructor
public class RequestReturningController {

	private final RequestReturningService requestReturningService;

	@GetMapping
	public ResponseEntity<ListRequestReturningResponseDto> getListRequestReturning(
			@RequestParam(name = "page") Integer page, @RequestParam(name = "size") Integer size,
			@RequestParam(required = false, defaultValue = "", value = "keyword") String keyword,
			@RequestParam(required = false, defaultValue = "", value = "sortBy") String sortBy,
			@RequestParam(required = false, defaultValue = "", value = "sortDirection") String sortDirection,
			@RequestParam(required = false, defaultValue = "", value = "returnDate") String returnDate,
			@RequestParam(required = false, defaultValue = "", value = "states") List<String> states) {
		RequestReturningRequestGetListDto dto = new RequestReturningRequestGetListDto(states, returnDate, keyword,
				sortBy, sortDirection, page, size);
		ListRequestReturningResponseDto result = requestReturningService.getListRequestReturning(dto);
		return new ResponseEntity<ListRequestReturningResponseDto>(result, HttpStatus.OK);
	}

	@GetMapping("/states")
	public ResponseEntity<HashMap<String, String>> getRequestReturningState() {
		HashMap<String, String> hashMap = RequestReturningState.getRequestReturningState();
		return new ResponseEntity<>(hashMap, HttpStatus.OK);
    }


	@Operation(summary = "Update assets with new information",
			description = "Users provide new information of asset then asset will be updated")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "UPDATED - Successfully updated the returning request"),
			@ApiResponse(responseCode = "400",
					description = "Bad Request - The request is invalid",
					content = {@Content(examples = {@ExampleObject()})}),
			@ApiResponse(responseCode = "401", description = "UNAUTHORIZED - The request is unauthorized"),
			@ApiResponse(responseCode = "403", description = "FORBIDDEN - You don’t have permission to access"),
			@ApiResponse(responseCode = "404", description = "NOT FOUND - The assignment with given request is not found"),
			@ApiResponse(responseCode = "500",
					description = "Internal Error - There were some error while processing in server",
					content = {@Content(examples = {@ExampleObject()})})
	})
    @PutMapping("/states/complete")
	@ResponseStatus(HttpStatus.OK)
    public RequestReturningResponseDto completeReturningRequest(@Valid @RequestBody ReturningRequestDto request) {
		return requestReturningService.completeReturningRequest(request);

	}
}
