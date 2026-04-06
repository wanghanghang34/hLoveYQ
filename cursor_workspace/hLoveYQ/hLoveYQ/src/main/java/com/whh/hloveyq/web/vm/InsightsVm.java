package com.whh.hloveyq.web.vm;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InsightsVm {
	private String nickname;
	private LocalDate asOfDate;

	private Integer avgCycleDays;
	private Integer avgPeriodLenDays;

	private LocalDate lastPeriodStart;
	private LocalDate lastPeriodEnd;
	private Integer lastPeriodLenDays;

	private LocalDate nextStartDate;
	private LocalDate nextStartFrom;
	private LocalDate nextStartTo;
	private String predictionBasis;

	private Integer cycleMinDays;
	private Integer cycleMaxDays;
	private Double cycleStdDays;
	private List<Integer> last3CycleDays = new ArrayList<>();

	private List<LevelCount> flowHistogram = new ArrayList<>();
	private Double avgPainLevel;
	private Integer lastPainLevel;

	private List<AlertVm> alerts = new ArrayList<>();

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public LocalDate getAsOfDate() {
		return asOfDate;
	}

	public void setAsOfDate(LocalDate asOfDate) {
		this.asOfDate = asOfDate;
	}

	public Integer getAvgCycleDays() {
		return avgCycleDays;
	}

	public void setAvgCycleDays(Integer avgCycleDays) {
		this.avgCycleDays = avgCycleDays;
	}

	public Integer getAvgPeriodLenDays() {
		return avgPeriodLenDays;
	}

	public void setAvgPeriodLenDays(Integer avgPeriodLenDays) {
		this.avgPeriodLenDays = avgPeriodLenDays;
	}

	public LocalDate getLastPeriodStart() {
		return lastPeriodStart;
	}

	public void setLastPeriodStart(LocalDate lastPeriodStart) {
		this.lastPeriodStart = lastPeriodStart;
	}

	public LocalDate getLastPeriodEnd() {
		return lastPeriodEnd;
	}

	public void setLastPeriodEnd(LocalDate lastPeriodEnd) {
		this.lastPeriodEnd = lastPeriodEnd;
	}

	public Integer getLastPeriodLenDays() {
		return lastPeriodLenDays;
	}

	public void setLastPeriodLenDays(Integer lastPeriodLenDays) {
		this.lastPeriodLenDays = lastPeriodLenDays;
	}

	public LocalDate getNextStartDate() {
		return nextStartDate;
	}

	public void setNextStartDate(LocalDate nextStartDate) {
		this.nextStartDate = nextStartDate;
	}

	public LocalDate getNextStartFrom() {
		return nextStartFrom;
	}

	public void setNextStartFrom(LocalDate nextStartFrom) {
		this.nextStartFrom = nextStartFrom;
	}

	public LocalDate getNextStartTo() {
		return nextStartTo;
	}

	public void setNextStartTo(LocalDate nextStartTo) {
		this.nextStartTo = nextStartTo;
	}

	public String getPredictionBasis() {
		return predictionBasis;
	}

	public void setPredictionBasis(String predictionBasis) {
		this.predictionBasis = predictionBasis;
	}

	public Integer getCycleMinDays() {
		return cycleMinDays;
	}

	public void setCycleMinDays(Integer cycleMinDays) {
		this.cycleMinDays = cycleMinDays;
	}

	public Integer getCycleMaxDays() {
		return cycleMaxDays;
	}

	public void setCycleMaxDays(Integer cycleMaxDays) {
		this.cycleMaxDays = cycleMaxDays;
	}

	public Double getCycleStdDays() {
		return cycleStdDays;
	}

	public void setCycleStdDays(Double cycleStdDays) {
		this.cycleStdDays = cycleStdDays;
	}

	public List<Integer> getLast3CycleDays() {
		return last3CycleDays;
	}

	public void setLast3CycleDays(List<Integer> last3CycleDays) {
		this.last3CycleDays = last3CycleDays;
	}

	public List<LevelCount> getFlowHistogram() {
		return flowHistogram;
	}

	public void setFlowHistogram(List<LevelCount> flowHistogram) {
		this.flowHistogram = flowHistogram;
	}

	public Double getAvgPainLevel() {
		return avgPainLevel;
	}

	public void setAvgPainLevel(Double avgPainLevel) {
		this.avgPainLevel = avgPainLevel;
	}

	public Integer getLastPainLevel() {
		return lastPainLevel;
	}

	public void setLastPainLevel(Integer lastPainLevel) {
		this.lastPainLevel = lastPainLevel;
	}

	public List<AlertVm> getAlerts() {
		return alerts;
	}

	public void setAlerts(List<AlertVm> alerts) {
		this.alerts = alerts;
	}
}

