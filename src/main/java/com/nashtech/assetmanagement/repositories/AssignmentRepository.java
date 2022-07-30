package com.nashtech.assetmanagement.repositories;

import com.nashtech.assetmanagement.entities.Assignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;

public interface AssignmentRepository extends JpaRepository<Assignment, String> {

    Page<Assignment> findByOrderByAssetCodeAsc(Pageable pageable);

    Page<Assignment> findByState(Pageable pageable, String state);
//    Page<Assignment> findByStateIgnoreCase
    Page<Assignment> findById_AssignedDate(Pageable pageable, Date assignedDate);

    Page<Assignment> findByStateAndId_AssignedDate(Pageable pageable, String state, Date assignedDate);

    @Query(value = "from Assignment a " +
            "where (lower(a.asset.code) like concat('%', :text, '%') " +
            "or lower(a.asset.name) like concat('%', :text, '%')" +
            "or lower(a.assignedTo.userName) like concat('%', :text, '%')) " +
            "order by a.asset.code asc ")
    Page<Assignment> searchByAssetCodeOrAssetNameOrUsernameAssignee(@Param("text") String textSearch, Pageable pageable);

    @Query(value = "from Assignment a " +
            "where  (lower(a.asset.code) like concat('%', :text, '%') " +
            "or lower(a.asset.name) like concat('%', :text, '%')" +
            "or lower(a.assignedTo.userName) like concat('%', :text, '%'))  " +
            "and lower(a.state) = :state " +
            "and a.id.assignedDate = :assignedDate "+
            "order by a.asset.code asc ")
    Page<Assignment> getAssignmentBySearchingOrFiltering(@Param("text") String textSearch,
                                                         @Param("state") String state,
                                                         @Param("assignedDate") Date assignedDate,
                                                         Pageable pageable);
}
