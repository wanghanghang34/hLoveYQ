package com.whh.hloveyq.service;

import com.whh.hloveyq.domain.PeriodRecord;
import com.whh.hloveyq.domain.User;
import com.whh.hloveyq.repo.PeriodRecordRepository;
import com.whh.hloveyq.web.form.PeriodRecordForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * 经期记录服务类
 * 负责经期记录的查询、创建等业务逻辑
 */
@Service
public class PeriodRecordService {
    
    private final PeriodRecordRepository periodRecordRepository;

    public PeriodRecordService(PeriodRecordRepository periodRecordRepository) {
        this.periodRecordRepository = periodRecordRepository;
    }

    /**
     * 查询用户最新的 12 条记录（按开始日期降序）
     * @param user 用户
     * @return 记录列表
     */
    public List<PeriodRecord> recent12(User user) {
        return periodRecordRepository.findTop12ByUserOrderByStartDateDesc(user);
    }

    /**
     * 查询用户最新的一条记录
     * @param user 用户
     * @return 最新记录，如果没有则返回 null
     */
    public PeriodRecord latest(User user) {
        return periodRecordRepository.findTop1ByUserOrderByStartDateDesc(user).orElse(null);
    }

    /**
     * 创建新的经期记录
     * @param user 用户
     * @param form 记录表单
     * @return 创建的记录
     * @throws IllegalArgumentException 当结束日期早于开始日期时抛出
     */
    @Transactional
    public PeriodRecord create(User user, PeriodRecordForm form) {
        LocalDate start = form.getStartDate();
        LocalDate end = form.getEndDate();
        if (end != null && end.isBefore(start)) {
            throw new IllegalArgumentException("结束日期不能早于开始日期");
        }
        PeriodRecord record = new PeriodRecord(
                user,
                start,
                end,
                form.getFlowLevel(),
                form.getPainLevel(),
                (form.getNote() == null ? null : form.getNote().trim()),
                Instant.now()
        );
        return periodRecordRepository.save(record);
    }
}
