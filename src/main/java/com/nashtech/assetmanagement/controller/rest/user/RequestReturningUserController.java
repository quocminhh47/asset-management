package com.nashtech.assetmanagement.controller.rest.user;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.assetmanagement.dto.request.CreateRequestReturningAssetRequestDto;
import com.nashtech.assetmanagement.dto.response.CreateRequestReturningResponseDto;
import com.nashtech.assetmanagement.service.RequestReturningService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@Tag(name = "Request Returning Resources",
        description = "Provide the all request returning of the current user")
@RestController
@AllArgsConstructor
@RequestMapping("/users/api/requests")
public class RequestReturningUserController {
    private RequestReturningService requestReturningService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new request returning",
            description = "Create new request returning with requestedBy username, acceptedBy username, assetCode, assignedDate, assignedTo username, returnedDate, state")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED - Successfully created request returning"),
            @ApiResponse(responseCode = "400",
                    description = "Bad Request - The request is invalid",
                    content = {@Content(examples = {@ExampleObject()})}),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED - The request is unauthorized"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN - You don’t have permission to access"),
            @ApiResponse(responseCode = "500",
                    description = "Internal Error - There were some error while processing in server",
                    content = {@Content(examples = {@ExampleObject()})})
    })
    public CreateRequestReturningResponseDto createRequestReturning(@Valid @RequestBody CreateRequestReturningAssetRequestDto createRequestReturningAssetRequestDto) {
        return requestReturningService.createRequestReturningAsset(createRequestReturningAssetRequestDto);
    }

}