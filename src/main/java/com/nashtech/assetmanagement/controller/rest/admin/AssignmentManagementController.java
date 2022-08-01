package com.nashtech.assetmanagement.controller.rest.admin;

import com.nashtech.assetmanagement.dto.request.RequestAssignmentDTO;
import com.nashtech.assetmanagement.dto.response.AssignmentDto;
import com.nashtech.assetmanagement.dto.response.ListAssignmentResponse;
import com.nashtech.assetmanagement.service.AssignmentService;
import com.nashtech.assetmanagement.utils.AppConstants;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@AllArgsConstructor
@RequestMapping("/admin/api/assignment")
public class AssignmentManagementController {

    private final AssignmentService assignmentService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ListAssignmentResponse getAssignmentListOrderByDefaultOrFilter(
            @RequestParam(
                    value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false)
            int pageNo,
            @RequestParam(
                    value = "pageSize", defaultValue = "20", required = false)
            int pageSize,
            @RequestParam(
                    value = "sortBy", defaultValue = "assetCode", required = false)
            String sortBy,
            @RequestParam(
                    value = "sortDirection", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false)
            String sortDirection,
            @RequestParam(value = "state", defaultValue = "", required = false) String state,
            @RequestParam(value = "assigned-date", required = false) String assignedDate
    ) {
        return assignmentService.getAllAssignmentByStateOrAssignedDate(
                pageNo,
                pageSize,
                sortBy,
                sortDirection,
                state,
                assignedDate);
    }

    @GetMapping("/searching")
    @ResponseStatus(HttpStatus.OK)
    public ListAssignmentResponse getAssignmentListOrderBySearching(
            @RequestParam(
                    value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false)
            int pageNo,
            @RequestParam(
                    value = "pageSize", defaultValue = "20", required = false)
            int pageSize,
            @RequestParam(value = "text", defaultValue = "", required = false) String text
    ) {
        return assignmentService.getAssignmentsBySearching(pageNo, pageSize, text);
    }

    @PostMapping("/create")
    public ResponseEntity<AssignmentDto> createNewAssignment(@RequestBody RequestAssignmentDTO request) {
        return ResponseEntity.created(URI.create("/admin/api/assignment/create"))
                .body(this.assignmentService.createNewAssignment(request));
    }

}
