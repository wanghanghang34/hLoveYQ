package com.whh.hloveyq.repo;

import com.whh.hloveyq.domain.PeriodRecord;
import com.whh.hloveyq.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PeriodRecordRepository extends JpaRepository<PeriodRecord, Long> {
	List<PeriodRecord> findTop12ByUserOrderByStartDateDesc(User user);
	Optional<PeriodRecord> findTop1ByUserOrderByStartDateDesc(User user);

	/**
	 * Query records overlapping a date range.
	 *
	 * <p>We use "overlap" rather than "contained in" because a record can start
	 * before the current month and end inside the month (or end after the month).</p>
	 *
	 * <p>Rules:
	 * - startDate <= rangeEnd
	 * - and (endDate is null OR endDate >= rangeStart)</p>
	 */
	@Query("""
			select r
			from PeriodRecord r
			where r.user = :user
			  and r.startDate <= :rangeEnd
			  and (r.endDate is null or r.endDate >= :rangeStart)
			order by r.startDate asc
			""")
	List<PeriodRecord> findOverlappingRange(@Param("user") User user,
	                                       @Param("rangeStart") LocalDate rangeStart,
	                                       @Param("rangeEnd") LocalDate rangeEnd);
}

