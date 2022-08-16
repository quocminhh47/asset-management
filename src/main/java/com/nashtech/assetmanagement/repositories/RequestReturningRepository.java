package com.nashtech.assetmanagement.repositories;

import com.nashtech.assetmanagement.entities.Assignment;
import com.nashtech.assetmanagement.entities.RequestReturning;
import com.nashtech.assetmanagement.enums.RequestReturningState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RequestReturningRepository extends JpaRepository<RequestReturning, Long> {

	@Query("select e from RequestReturning e where"
			+ "( (lower(e.assignment.asset.code) like lower(concat('%', :search, '%')))  or "
			+ "  (lower(e.assignment.asset.name) like lower(concat('%', :search, '%')))  or"
			+ "  (lower(e.requestedBy.userName) like lower(concat('%', :search, '%'))) )"
			+ "and (e.state in :state) and  (:returnedDate is null or e.returnedDate = to_date(:returnedDate, 'YYYY-MM-dd'))")
	Page<RequestReturning> getListRequestReturning(@Param("state") List<RequestReturningState> state,
			@Param("returnedDate") String returnedDate, @Param("search") String search, Pageable pageable);

	@Query("SELECT b FROM RequestReturning b where (b.state in :state) and (:returnedDate is null or b.returnedDate = to_date(:returnedDate, 'YYYY-MM-dd')) "
			+ "and ((lower(b.assignment.asset.code) like lower(concat('%', :search, '%')))  or "
			+ "  (lower(b.assignment.asset.name) like lower(concat('%', :search, '%')))  or"
			+ "  (lower(b.requestedBy.userName) like lower(concat('%', :search, '%'))))"
			+ "  ORDER BY b.acceptedBy DESC NULLS LAST")
	Page<RequestReturning> getListSortByAcceptedByDESC(@Param("state") List<RequestReturningState> state,
			@Param("returnedDate") String returnedDate, @Param("search") String search, Pageable pageable);

	@Query("SELECT b FROM RequestReturning b where (b.state in :state) and (:returnedDate is null or b.returnedDate = to_date(:returnedDate, 'YYYY-MM-dd')) "
			+ "and ((lower(b.assignment.asset.code) like lower(concat('%', :search, '%')))  or "
			+ "  (lower(b.assignment.asset.name) like lower(concat('%', :search, '%')))  or"
			+ "  (lower(b.requestedBy.userName) like lower(concat('%', :search, '%'))))"
			+ "  ORDER BY b.acceptedBy ASC NULLS FIRST")
	Page<RequestReturning> getListSortByAcceptedByASC(@Param("state") List<RequestReturningState> state,
			@Param("returnedDate") String returnedDate, @Param("search") String search, Pageable pageable);

	Optional<RequestReturning> getRequestReturningByAssignment(Assignment assignment);
	
	Optional<RequestReturning> findByAssignment (Assignment assignment);
}
