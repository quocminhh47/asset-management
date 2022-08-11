package com.nashtech.assetmanagement.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.nashtech.assetmanagement.entities.Assignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nashtech.assetmanagement.entities.RequestReturning;
import com.nashtech.assetmanagement.enums.RequestReturningState;

public interface RequestReturningRepository extends JpaRepository<RequestReturning, Long> {

	@Query("select e from RequestReturning e where"
			+ "( (lower(e.assignment.asset.code) like lower(concat('%', :search, '%')))  or "
			+ "  (lower(e.assignment.asset.name) like lower(concat('%', :search, '%')))  or"
			+ "  (lower(e.requestedBy.staffCode) like lower(concat('%', :search, '%'))) )"
			+ "and (e.state in :state) and (e.returnedDate = :returnedDate)")
	Page<RequestReturning> getListRequestReturning(@Param("state") List<RequestReturningState> state,
			@Param("returnedDate") Date returnedDate, @Param("search") String search, Pageable pageable);

	//587 - Create request for returning asset
	Optional<RequestReturning> getRequestReturningByAssignment(Assignment assignment);


	@Query("select e from RequestReturning e where"
			+ "( (lower(e.assignment.asset.code) like lower(concat('%', :search, '%')))  or "
			+ "  (lower(e.assignment.asset.name) like lower(concat('%', :search, '%')))  or"
			+ "  (lower(e.requestedBy.staffCode) like lower(concat('%', :search, '%'))) )"
			+ "and (e.state in :state)")
	Page<RequestReturning> getListRequestReturningByStates(@Param("state") List<RequestReturningState> state,
			@Param("search") String search, Pageable pageable);

}
