package com.nashtech.assetmanagement.repositories;

import com.nashtech.assetmanagement.entities.Asset;
import com.nashtech.assetmanagement.entities.Assignment;
import com.nashtech.assetmanagement.entities.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, String> {

    Page<Assignment> findByOrderByAssetCodeAsc(Pageable pageable);

    Page<Assignment> findByState(Pageable pageable, String state);


    Page<Assignment> findById_AssignedDate(Pageable pageable, Date assignedDate);

    Page<Assignment> findByStateAndId_AssignedDate(Pageable pageable, String state, Date assignedDate);

    List<Assignment> findByAsset(Asset asset);

    Boolean existsByAssignedToOrAssignedBy(Users assignedTo, Users assignedBy);

    @Query(value = "from Assignment a where " +
            "(lower(a.asset.code)  like concat('%', :text, '%')" +
            "or lower(a.asset.name) like concat('%', :text, '%')" +
            "or lower(a.assignedBy.userName) like concat('%', :text, '%'))" +
            "and a.state in :states " +
            "and a.id.assignedDate = :assignedDate " +
            "order by a.asset.code asc ")
    Page<Assignment> getAssignmentByConditions(@Param("text") String textSearch,
                                               @Param("states") List<String> states,
                                               @Param("assignedDate") Date assignedDate,
                                               Pageable pageable);

    @Query(value = "from Assignment a where " +
            "(lower(a.asset.code)  like concat('%', :text, '%')" +
            "or lower(a.asset.name) like concat('%', :text, '%')" +
            "or lower(a.assignedBy.userName) like concat('%', :text, '%'))" +
            "and a.state in :states " +
            "order by a.asset.code asc " )
    Page<Assignment> getAssignmentWithoutAssignedDate(@Param("text") String textSearch,
                                               @Param("states") List<String> states,
                                               Pageable pageable);
}
