package com.nashtech.assetmanagement.controller.rest.admin;

import com.nashtech.assetmanagement.dto.request.RequestAssignmentDTO;
import com.nashtech.assetmanagement.dto.response.ResponseAssignmentDto;
import com.nashtech.assetmanagement.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/admin/api/assignment")
public class AssignmentController {

    private final AssignmentService assignmentService;
    @Autowired
    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseAssignmentDto> createNewAssignment(@RequestBody RequestAssignmentDTO request){
        return ResponseEntity.created(URI.create("/admin/api/assignment/create"))
                .body(this.assignmentService.createNewAssignment(request));
    }
}
