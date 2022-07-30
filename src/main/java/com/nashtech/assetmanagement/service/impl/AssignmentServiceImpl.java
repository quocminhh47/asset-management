package com.nashtech.assetmanagement.service.impl;

import com.nashtech.assetmanagement.dto.response.ListAssignmentResponse;
import com.nashtech.assetmanagement.entities.Assignment;
import com.nashtech.assetmanagement.exception.DateInvalidException;
import com.nashtech.assetmanagement.mapper.AssignmentContent;
import com.nashtech.assetmanagement.repositories.AssignmentRepository;
import com.nashtech.assetmanagement.service.AssignmentService;
import com.nashtech.assetmanagement.utils.StateConverter;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.nashtech.assetmanagement.dto.request.RequestAssignmentDTO;
import com.nashtech.assetmanagement.dto.response.ResponseAssignmentDto;
import com.nashtech.assetmanagement.entities.Asset;
import com.nashtech.assetmanagement.entities.Assignment;
import com.nashtech.assetmanagement.entities.Users;
import com.nashtech.assetmanagement.enums.AssetState;
import com.nashtech.assetmanagement.exception.ResourceNotFoundException;
import com.nashtech.assetmanagement.mapper.AssignmentMapper;
import com.nashtech.assetmanagement.repositories.AssetRepository;
import com.nashtech.assetmanagement.repositories.AssignmentRepository;
import com.nashtech.assetmanagement.repositories.UserRepository;
import com.nashtech.assetmanagement.service.AssignmentService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.sql.Date;

@Service
@AllArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final AssignmentContent assignmentContent;


        private final UserRepository userRepository;
        private final AssetRepository assetRepository;
        private final AssignmentMapper assignmentMapper;



/*    @Override
    public ListAssignmentResponse getAllAssignmentsOrderByAssetCode(int pageNo,
                                                                    int pageSize,
                                                                    String sortBy,
                                                                    String sortDirection) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, defaultSorting(sortBy, sortDirection));
        Page<Assignment> assignmentPage = assignmentRepository.findByOrderByAssetCodeAsc(pageable);
        return assignmentContent.getAssignmentResponse(assignmentPage);
    }*/

    /*@Override
    public ListAssignmentResponse getAssignmentsBySearchingOrFiltering(int pageNo,
                                                                       int pageSize,
                                                                       String state,
                                                                       String assignedDateStr,
                                                                       String textSearch) {
        Date assignedDate = null;
        if (assignedDateStr != null) {
            try {
                assignedDate = Date.valueOf(assignedDateStr); //pattern: yyyy/mm/dd
            } catch (Exception e) {
                e.printStackTrace();
                throw new DateInvalidException(assignedDateStr + ".is.not.in.the.right.format", e);
            }
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Assignment> assignmentPage =
                assignmentRepository.getAssignmentBySearchingOrFiltering(textSearch, state, assignedDate, pageable);

        return assignmentContent.getAssignmentResponse(assignmentPage);
    }*/

    @Override
    public ListAssignmentResponse getAllAssignmentByStateOrAssignedDate(int pageNo,
                                                                        int pageSize,
                                                                        String sortBy,
                                                                        String sortDirection,
                                                                        String state,
                                                                        String assignedDateStr) {
        String parsedState = StateConverter.getAssignmentState(state);
        System.out.println(parsedState);
        Pageable pageable = PageRequest.of(pageNo, pageSize, defaultSorting(sortBy, sortDirection));
        Page<Assignment> assignmentPage = null;
        Date assignedDate;

        if (assignedDateStr != null) {
            try {
                assignedDate = Date.valueOf(assignedDateStr); //pattern: yyyy/mm/dd
                System.out.println(assignedDate);
                Date dateNow = new Date(new java.util.Date().getTime());
                System.out.println(dateNow);
                if(assignedDate.after(dateNow)) throw new DateInvalidException(
                        "Date.is.must.before.today:"+ dateNow.toString().replaceAll(" ", "."));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("Date.format.is.not.valid", e);
            }

            //filter by assigned date
            if (parsedState == null && assignedDateStr != null) {
                assignmentPage = assignmentRepository.findById_AssignedDate(pageable, assignedDate);
            }

            //filter by both of them
            else if (assignedDateStr != null && parsedState != null) {
                assignmentPage = assignmentRepository.findByStateAndId_AssignedDate(pageable, parsedState, assignedDate);
            }
        }

        //filter by state
        if (assignedDateStr == null && parsedState != null) {
            assignmentPage = assignmentRepository.findByState(pageable, parsedState);
        }

        //find all
        else if (assignedDateStr == null && parsedState == null) {
            assignmentPage = assignmentRepository.findByOrderByAssetCodeAsc(pageable);
        }

        return assignmentContent.getAssignmentResponse(assignmentPage);
    }


    @Override
    public ListAssignmentResponse getAssignmentsBySearching(int pageNo,
                                                            int pageSize,
                                                            String textSearch) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Assignment> assignmentPage = assignmentRepository.searchByAssetCodeOrAssetNameOrUsernameAssignee(
                textSearch.replaceAll(" ", "").toLowerCase(), pageable);
        return assignmentContent.getAssignmentResponse(assignmentPage);

    }

    public Sort defaultSorting(String sortBy, String sortDirection) {
        return sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
    }






    @Override
    public ResponseAssignmentDto createNewAssignment(RequestAssignmentDTO request) {
        Optional<Asset> asset = assetRepository.findById(request.getAssetCode());
        if(asset.isEmpty()){
            throw new ResourceNotFoundException("AssetCode not found");
        }
        Optional<Users> assignTo = userRepository.findById(request.getAssignedTo());
        if(assignTo.isEmpty()){
            throw new ResourceNotFoundException("Assign to User not found");
        }
        Optional<Users> assignBy = userRepository.findById(request.getAssignedBy());
        if (assignBy.isEmpty()){
            throw new ResourceNotFoundException("Assign by User not found");
        }
        asset.get().setState(AssetState.ASSIGNED);
        Assignment assignment = assignmentMapper.MapRequestAssignmentToAssignment(request);
        assignment.setAssignedTo(assignTo.get());
        assignment.setAssignedBy(assignBy.get());
        assignment.setAsset(asset.get());
        assignmentRepository.save(assignment);
        return assignmentMapper.MapAssignmentToResponseDto(assignment);
    }
}
