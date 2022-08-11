package com.nashtech.assetmanagement.controller.rest.admin;

import com.nashtech.assetmanagement.dto.DeleteAssignmentRequestDto;
import com.nashtech.assetmanagement.dto.request.AssignmentRequestDto;
import com.nashtech.assetmanagement.dto.response.AssignmentResponseDto;
import com.nashtech.assetmanagement.dto.response.ListAssignmentResponseDto;
import com.nashtech.assetmanagement.dto.request.EditAssignmentRequestDto;
import com.nashtech.assetmanagement.dto.response.MessageResponse;
import com.nashtech.assetmanagement.service.AssignmentService;
import com.nashtech.assetmanagement.utils.AppConstants;
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

import java.net.URI;
import java.sql.Date;
import java.util.List;

@Tag(name = "Assignment Resources Management",
        description = "Provide the ability of assignment management and information")
@RestController
@AllArgsConstructor
@RequestMapping("/admin/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    @Operation(summary = "Get assignment list",
            description = "This provide assignment list in pagination by default or filtered")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Successfully retrieved"),
            @ApiResponse(responseCode = "400",
                    description = "Bad Request - The request is invalid",
                    content = {@Content(examples = {@ExampleObject()})}),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED - The request is unauthorized"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN - You don’t have permission to access"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND - The assignment resource is not found"),
            @ApiResponse(responseCode = "500",
                    description = "Internal Error - There were some error while processing in server",
                    content = {@Content(examples = {@ExampleObject()})})
    })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ListAssignmentResponseDto getAssignmentListOrderByDefaultOrFilter(
            @RequestParam(
                    value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false)
            int pageNo,
            @RequestParam(
                    value = "pageSize", defaultValue = "20", required = false)
            int pageSize,
            @RequestParam(value = "text", defaultValue = "", required = false) String text,
            @RequestParam(value = "state", defaultValue = "", required = false) List<String> states,
            @RequestParam(value = "assigned-date", defaultValue = "", required = false) String assignedDate
    ) {
        return assignmentService
                .getAssignmentsByCondition(pageNo, pageSize, text, states, assignedDate);
    }


    @Operation(summary = "Create new assignment feature",
            description = "Create new assignment with all properties given")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED - Successfully created assignment"),
            @ApiResponse(responseCode = "400",
                    description = "Bad Request - The request is invalid",
                    content = {@Content(examples = {@ExampleObject()})}),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED - The request is unauthorized"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN - You don’t have permission to access"),
            @ApiResponse(responseCode = "500",
                    description = "Internal Error - There were some error while processing in server",
                    content = {@Content(examples = {@ExampleObject()})})
    })
    @PostMapping()
    public ResponseEntity<AssignmentResponseDto> createNewAssignment(@RequestBody AssignmentRequestDto request) {
        return ResponseEntity.created(URI.create("/admin/api/assignments"))
                .body(this.assignmentService.createNewAssignment(request));
    }

    @Operation(summary = "Edit assignment feature",
            description = "Edit assignment with all properties given")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Successfully edit assignment"),
            @ApiResponse(responseCode = "400",
                    description = "Bad Request - The request is invalid",
                    content = {@Content(examples = {@ExampleObject()})}),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED - The request is unauthorized"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN - You don’t have permission to access"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND - The assignment resource is not found"),
            @ApiResponse(responseCode = "500",
                    description = "Internal Error - There were some error while processing in server",
                    content = {@Content(examples = {@ExampleObject()})})
    })
    @PutMapping()
    public ResponseEntity<AssignmentResponseDto> editAssignment(@RequestBody EditAssignmentRequestDto request){
        return ResponseEntity.ok(this.assignmentService.editAssignment(request));
    }

    @Operation(summary = "Get list assignment of the specified asset",
            description = "Given asset code then this will return list assignment of the given asset")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Successfully retrieved"),
            @ApiResponse(responseCode = "400",
                    description = "Bad Request - The request is invalid",
                    content = {@Content(examples = {@ExampleObject()})}),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED - The request is unauthorized"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN - You don’t have permission to access"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND - The assignment resource of this asset is not found"),
            @ApiResponse(responseCode = "500",
                    description = "Internal Error - There were some error while processing in server",
                    content = {@Content(examples = {@ExampleObject()})})
    })
    @GetMapping("/{assetId}")
    public ResponseEntity<List<AssignmentResponseDto>> getListAssignmentByAsset(@PathVariable("assetId") String assetId) {
        return new ResponseEntity<>(assignmentService.getListAssignmentByAssetCode(assetId), HttpStatus.OK);
    }

    @Operation(summary = "Delete assignment",
            description = "Delete assignment which had state 'Waiting for acceptance' " +
                    "or 'Decline'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Successfully retrieved"),
            @ApiResponse(responseCode = "400",
                    description = "Bad Request - The request is invalid"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED - The request is unauthorized"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN - You don’t have permission to access"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND - The " +
                    "assignment or asset resource is not found"),
            @ApiResponse(responseCode = "406", description = "NOT_ACCEPTABLE - The " +
                    "state of the assignment is 'Accepted'"),
            @ApiResponse(responseCode = "500",
                    description = "Internal Error - There were some error while processing in server")
    })
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public MessageResponse deleteAssignment(
            @RequestParam(value = "assignedTo") String assignedTo,
            @RequestParam(value = "assetCode") String assetCode,
            @RequestParam(value = "assignedDate") Date assignedDate
    ) {
        DeleteAssignmentRequestDto deleteAssignmentRequestDto =
                new DeleteAssignmentRequestDto(assignedTo, assetCode, assignedDate);
        return assignmentService.deleteAssignment(deleteAssignmentRequestDto);
    }
}
