package com.nashtech.assetmanagement.service;

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
}
