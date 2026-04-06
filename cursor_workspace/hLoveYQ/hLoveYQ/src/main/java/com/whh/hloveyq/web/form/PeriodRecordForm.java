package com.whh.hloveyq.web.form;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class PeriodRecordForm {
	@NotNull
	private LocalDate startDate;

	private LocalDate endDate;

	@Min(1)
	@Max(5)
	private Integer flowLevel;

	@Min(0)
	@Max(10)
	private Integer painLevel;

	@Size(max = 500)
	private String note;

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
}

