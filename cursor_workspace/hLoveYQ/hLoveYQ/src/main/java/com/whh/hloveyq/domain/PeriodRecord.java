package com.whh.hloveyq.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.time.LocalDate;

/**
 * 经期记录实体类
 * 对应数据库表：period_records
 */
@Entity
@Table(name = "period_records")
public class PeriodRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "用户不能为空")
    private User user;

    @NotNull(message = "开始日期不能为空")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Min(value = 1, message = "流量等级最小为 1")
    @Max(value = 5, message = "流量等级最大为 5")
    @Column(name = "flow_level")
    private Integer flowLevel;

    @Min(value = 1, message = "疼痛等级最小为 1")
    @Max(value = 5, message = "疼痛等级最大为 5")
    @Column(name = "pain_level")
    private Integer painLevel;

    @Size(max = 500, message = "备注长度不能超过 500 个字符")
    @Column(length = 500)
    private String note;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected PeriodRecord() {}

    public PeriodRecord(User user, LocalDate startDate, LocalDate endDate, Integer flowLevel, Integer painLevel, String note, Instant createdAt) {
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.flowLevel = flowLevel;
        this.painLevel = painLevel;
        this.note = note;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getFlowLevel() {
        return flowLevel;
    }

    public void setFlowLevel(Integer flowLevel) {
        this.flowLevel = flowLevel;
    }

    public Integer getPainLevel() {
        return painLevel;
    }

    public void setPainLevel(Integer painLevel) {
        this.painLevel = painLevel;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}

