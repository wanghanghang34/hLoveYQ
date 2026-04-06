package com.whh.hloveyq.web.vm;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CalendarVm {
	private String nickname;
	private String title;

	private int year;
	private int month;

	private int prevYear;
	private int prevMonth;
	private int nextYear;
	private int nextMonth;

	private LocalDate predictedFrom;
	private LocalDate predictedTo;

	private List<List<CalendarDayVm>> weeks = new ArrayList<>();

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getPrevYear() {
		return prevYear;
	}

	public void setPrevYear(int prevYear) {
		this.prevYear = prevYear;
	}

	public int getPrevMonth() {
		return prevMonth;
	}

	public void setPrevMonth(int prevMonth) {
		this.prevMonth = prevMonth;
	}

	public int getNextYear() {
		return nextYear;
	}

	public void setNextYear(int nextYear) {
		this.nextYear = nextYear;
	}

	public int getNextMonth() {
		return nextMonth;
	}

	public void setNextMonth(int nextMonth) {
		this.nextMonth = nextMonth;
	}

	public LocalDate getPredictedFrom() {
		return predictedFrom;
	}

	public void setPredictedFrom(LocalDate predictedFrom) {
		this.predictedFrom = predictedFrom;
	}

	public LocalDate getPredictedTo() {
		return predictedTo;
	}

	public void setPredictedTo(LocalDate predictedTo) {
		this.predictedTo = predictedTo;
	}

	public List<List<CalendarDayVm>> getWeeks() {
		return weeks;
	}

	public void setWeeks(List<List<CalendarDayVm>> weeks) {
		this.weeks = weeks;
	}
}

