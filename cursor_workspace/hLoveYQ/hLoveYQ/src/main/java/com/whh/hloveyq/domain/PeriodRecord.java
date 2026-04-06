package com.whh.hloveyq.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "period_records")
public class PeriodRecord {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	@Column(name = "end_date")
	private LocalDate endDate;

	@Column(name = "flow_level")
	private Integer flowLevel;

	@Column(name = "pain_level")
	private Integer painLevel;

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

