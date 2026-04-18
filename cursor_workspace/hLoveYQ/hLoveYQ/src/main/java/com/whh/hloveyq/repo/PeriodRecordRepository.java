package com.whh.hloveyq.repo;

import com.whh.hloveyq.domain.PeriodRecord;
import com.whh.hloveyq.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 经期记录数据访问接口
 */
public interface PeriodRecordRepository extends JpaRepository<PeriodRecord, Long> {
    
    /**
     * 查询用户最新的 12 条记录（按开始日期降序）
     */
    List<PeriodRecord> findTop12ByUserOrderByStartDateDesc(User user);
    
    /**
     * 查询用户最新的一条记录
     */
    Optional<PeriodRecord> findTop1ByUserOrderByStartDateDesc(User user);

    /**
     * 查询指定日期范围内有重叠的记录
     * <p>
     * 使用"重叠"而非"包含"逻辑，因为记录可能：
     * - 在当前月份开始之前就开始，但在当前月份内结束
     * - 在当前月份开始，但在当前月份结束后才结束
     * </p>
     * <p>
     * 规则：
     * - startDate <= rangeEnd
     * - endDate is null OR endDate >= rangeStart
     * </p>
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
