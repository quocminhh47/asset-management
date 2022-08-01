package com.nashtech.assetmanagement.service;

import java.util.List;

import com.nashtech.assetmanagement.dto.response.AssignmentDto;
import com.nashtech.assetmanagement.dto.response.ListAssignmentResponse;

public interface AssignmentService {

   /* ListAssignmentResponse getAllAssignmentsOrderByAssetCode(int pageNo,
                                                             int pageSize,
                                                             String sortBy,
                                                             String sortDirection);*/

    /*ListAssignmentResponse getAssignmentsBySearchingOrFiltering(int pageNo,
                                                                int pageSize,
                                                                String state,
                                                                String assignedDateStr,
                                                                String textSearch);*/

    ListAssignmentResponse getAllAssignmentByStateOrAssignedDate(int pageNo,
                                                                 int pageSize,
                                                                 String sortBy,
                                                                 String sortDirection,
                                                                 String state,
                                                                 String assignedDateStr);

    ListAssignmentResponse getAssignmentsBySearching(int pageNo,
                                                     int pageSize,
                                                     String textSearch);

	List<AssignmentDto> getListAssignmentByAssetCode(String assetCode);
}
