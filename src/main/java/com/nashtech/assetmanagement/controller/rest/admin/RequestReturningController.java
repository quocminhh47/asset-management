package com.nashtech.assetmanagement.controller.rest.admin;

import com.nashtech.assetmanagement.dto.request.GetRequestReturningListRequestDto;
import com.nashtech.assetmanagement.dto.request.ReturningRequestDto;
import com.nashtech.assetmanagement.dto.response.ListRequestReturningResponseDto;
import com.nashtech.assetmanagement.dto.response.ListStateRequestReturningResponseDto;
import com.nashtech.assetmanagement.dto.response.MessageResponse;
import com.nashtech.assetmanagement.dto.response.RequestReturningResponseDto;
import com.nashtech.assetmanagement.service.RequestReturningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Tag(name = "Request returning Management")
@RequestMapping("/admin/api/requests")
public class RequestReturningController {

    @Autowired
    private RequestReturningService requestReturningService;

    @Operation(summary = "Get all request returning", description = "Admin can view all request returning")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK - Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Bad Request - The request is invalid", content = {
                    @Content(examples = {@ExampleObject()})}),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED - The request is unauthorized"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN - You don’t have permission to access"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND - The request returning resource is not found"),
            @ApiResponse(responseCode = "500", description = "Internal Error - There were some error while processing in server", content = {
                    @Content(examples = {@ExampleObject()})})})
    @GetMapping
    public ResponseEntity<ListRequestReturningResponseDto> getListRequestReturning(
            @RequestParam(name = "page") Integer page, @RequestParam(name = "size") Integer size,
            @RequestParam(required = false, defaultValue = "", value = "keyword") String keyword,
            @RequestParam(required = false, defaultValue = "", value = "sortBy") String sortBy,
            @RequestParam(required = false, defaultValue = "", value = "sortDirection") String sortDirection,
            @RequestParam(required = false, defaultValue = "", value = "returnDate") String returnDate,
            @RequestParam(required = false, defaultValue = "", value = "states") List<String> states) {
        GetRequestReturningListRequestDto dto = new GetRequestReturningListRequestDto(states, returnDate, keyword,
                sortBy, sortDirection, page, size);
        ListRequestReturningResponseDto result = requestReturningService.getListRequestReturning(dto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "Get all states of request returning", description = "This return all of the states of request returning entity")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK - Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Bad Request - The request is invalid",
                    content = {@Content(examples = {@ExampleObject()})}),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED - The request is unauthorized"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN - You don’t have permission to access"),
            @ApiResponse(responseCode = "404",
                    description = "NOT FOUND - The request returning state resource is not found"),
            @ApiResponse(responseCode = "500",
                    description = "Internal Error - There were some error while processing in server",
                    content = {@Content(examples = {@ExampleObject()})})})
    @GetMapping("/states")
    public ResponseEntity<ListStateRequestReturningResponseDto> getRequestReturningState() {
        ListStateRequestReturningResponseDto responseDto = requestReturningService.getRequestReturningState();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "Update assets with new information", description = "Users provide new information of asset then asset will be updated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "UPDATED - Successfully updated the returning request"),
            @ApiResponse(responseCode = "400", description = "Bad Request - The request is invalid",
                    content = {@Content(examples = {@ExampleObject()})}),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED - The request is unauthorized"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN - You don’t have permission to access"),
            @ApiResponse(responseCode = "404",
                    description = "NOT FOUND - The assignment with given request is not found"),
            @ApiResponse(responseCode = "500",
                    description = "Internal Error - There were some error while processing in server",
                    content = {@Content(examples = {@ExampleObject()})})})
    @PutMapping("/states/complete")
    @ResponseStatus(HttpStatus.OK)
    public RequestReturningResponseDto completeReturningRequest(@Valid @RequestBody ReturningRequestDto request) {
        return requestReturningService.completeReturningRequest(request);

    }

    @Operation(summary = "Cancel request returning", description = "Admin want to cancel returning request, so that admin can remove"
            + " request form request list when necessary it allows to cancel only"
            + " “Waiting for returning” requests")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK - Successfully "),
            @ApiResponse(responseCode = "400", description = "Bad Request - The request is invalid"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED - The request is unauthorized"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN - You don’t have permission to access"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND - The request resource is not found"),
            @ApiResponse(responseCode = "406",
                    description = "NOT_ACCEPTABLE - The state of the request is 'Completed'"),
            @ApiResponse(responseCode = "500",
                    description = "Internal Error - There were some error while processing in server")})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponse cancelRequestReturning(@PathVariable Long id) {
        return requestReturningService.cancelRequestReturningAssignment(id);
    }

}
