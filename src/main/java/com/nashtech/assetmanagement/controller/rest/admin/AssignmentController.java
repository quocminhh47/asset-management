package com.nashtech.assetmanagement.controller.rest.admin;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.assetmanagement.dto.request.AssignmentRequestDto;
import com.nashtech.assetmanagement.dto.response.AssignmentResponseDto;
import com.nashtech.assetmanagement.dto.response.ListAssignmentResponseDto;
import com.nashtech.assetmanagement.service.AssignmentService;
import com.nashtech.assetmanagement.utils.AppConstants;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/admin/api/assignment")
public class AssignmentController {

    private final AssignmentService assignmentService;

    @GetMapping("/all")
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
                .getAssignmentsByCondition(pageNo, pageSize,  text, states, assignedDate);
    }

    @PostMapping("/create")
    public ResponseEntity<AssignmentResponseDto> createNewAssignment(@RequestBody AssignmentRequestDto request) {
        return ResponseEntity.created(URI.create("/admin/api/assignment/create"))
                .body(this.assignmentService.createNewAssignment(request));
    }

    @GetMapping
    public ResponseEntity<List<AssignmentResponseDto>> getListAssignmentByAsset(@RequestParam(required = true, name = "assetId") String assetId) {
        return new ResponseEntity<List<AssignmentResponseDto>>(assignmentService.getListAssignmentByAssetCode(assetId), HttpStatus.OK);
    }
}
