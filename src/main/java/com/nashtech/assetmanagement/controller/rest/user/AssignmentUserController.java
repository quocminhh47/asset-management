package com.nashtech.assetmanagement.controller.rest.user;

import com.nashtech.assetmanagement.dto.request.ChangeAssignmentStateRequestDto;
import com.nashtech.assetmanagement.dto.response.AssignmentResponseDto;
import com.nashtech.assetmanagement.dto.response.MessageResponse;
import com.nashtech.assetmanagement.service.AssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    public ResponseEntity<List<AssignmentResponseDto>> getListAsset(
            @PathVariable("userId") String userId,
            @RequestParam(required = false, defaultValue = "", value = "sortBy") String sortBy,
            @RequestParam(required = false, defaultValue = "", value = "sortDirection") String sortDirection) {
        List<AssignmentResponseDto> result = assignmentService.getListAssignmentByUser(userId, sortBy, sortDirection);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Operation(summary = "Change state of assignments",
            description = "User is able to respond to his/her own assignments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Successfully updated"),
            @ApiResponse(responseCode = "400",
                    description = "Bad Request - Assignment state request is not valid",
                    content = {@Content(examples = {@ExampleObject()})}),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED - The request is unauthorized"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN - You don’t have permission to access"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND - The assignment is not found "),
            @ApiResponse(responseCode = "409",
                    description = "CONFLICT - Assignment state in database is Accepted or Declined"),
            @ApiResponse(responseCode = "500",
                    description = "Internal Error - There were some error while processing in server",
                    content = {@Content(examples = {@ExampleObject()})})
    })
    @PutMapping("/state")
    public ResponseEntity<MessageResponse> updateAssignmentStatus(@Valid @RequestBody ChangeAssignmentStateRequestDto changeAssignmentStateRequestDto) {
        MessageResponse messageResponse = assignmentService.updateAssignmentState(changeAssignmentStateRequestDto);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

}
