package com.whh.hloveyq.web.vm;

import java.time.LocalDate;

public class CalendarDayVm {
	private final LocalDate date;
	private final boolean inMonth;
	private final boolean today;
	private final boolean period;
	private final boolean predicted;
	private final boolean predictedWindow;

	public CalendarDayVm(LocalDate date,
	                     boolean inMonth,
	                     boolean today,
	                     boolean period,
	                     boolean predicted,
	                     boolean predictedWindow) {
		this.date = date;
		this.inMonth = inMonth;
		this.today = today;
		this.period = period;
		this.predicted = predicted;
		this.predictedWindow = predictedWindow;
	}

	public LocalDate getDate() {
		return date;
	}

	public boolean isInMonth() {
		return inMonth;
	}

	public boolean isToday() {
		return today;
	}

	public boolean isPeriod() {
		return period;
	}

	public boolean isPredicted() {
		return predicted;
	}

	public boolean isPredictedWindow() {
		return predictedWindow;
	}
}

