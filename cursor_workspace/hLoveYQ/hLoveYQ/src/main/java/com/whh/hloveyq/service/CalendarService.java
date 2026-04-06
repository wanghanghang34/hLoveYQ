package com.whh.hloveyq.service;

import com.whh.hloveyq.domain.PeriodRecord;
import com.whh.hloveyq.domain.User;
import com.whh.hloveyq.repo.PeriodRecordRepository;
import com.whh.hloveyq.web.vm.CalendarDayVm;
import com.whh.hloveyq.web.vm.CalendarVm;
import com.whh.hloveyq.web.vm.InsightsVm;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CalendarService {
	private final PeriodRecordRepository periodRecordRepository;
	private final InsightsService insightsService;

	public CalendarService(PeriodRecordRepository periodRecordRepository, InsightsService insightsService) {
		this.periodRecordRepository = periodRecordRepository;
		this.insightsService = insightsService;
	}

	/**
	 * Build a calendar month view.
	 *
	 * <p>Calendar is server-rendered (non-SPA). We render a typical month grid (6 weeks max),
	 * marking:
	 * - actual period days (from saved records)
	 * - predicted window (±3 days) and predicted period length (soft highlight)</p>
	 */
	public CalendarVm build(User user, YearMonth ym) {
		CalendarVm vm = new CalendarVm();
		vm.setNickname(user.getNickname());
		vm.setYear(ym.getYear());
		vm.setMonth(ym.getMonthValue());
		vm.setTitle(ym.format(DateTimeFormatter.ofPattern("yyyy 年 M 月")));

		YearMonth prev = ym.minusMonths(1);
		YearMonth next = ym.plusMonths(1);
		vm.setPrevYear(prev.getYear());
		vm.setPrevMonth(prev.getMonthValue());
		vm.setNextYear(next.getYear());
		vm.setNextMonth(next.getMonthValue());

		LocalDate monthStart = ym.atDay(1);
		LocalDate monthEnd = ym.atEndOfMonth();

		// For a consistent grid, show days from Monday..Sunday. (Chinese users are used to Monday-first.)
		LocalDate gridStart = startOfWeekMonday(monthStart);
		LocalDate gridEnd = endOfWeekSunday(monthEnd);

		// Fetch records overlapping the displayed grid (not only the month) so we can mark edge days.
		List<PeriodRecord> overlapping = periodRecordRepository.findOverlappingRange(user, gridStart, gridEnd);

		// Prediction comes from the same logic as insights.
		InsightsVm insights = insightsService.build(user);
		LocalDate predictedWindowFrom = insights.getNextStartFrom();
		LocalDate predictedWindowTo = insights.getNextStartTo();
		vm.setPredictedFrom(predictedWindowFrom);
		vm.setPredictedTo(predictedWindowTo);

		// Predicted period length: use avg period length if we have it; otherwise default to 5 days.
		int predictedLen = insights.getAvgPeriodLenDays() == null ? 5 : Math.max(2, Math.min(10, insights.getAvgPeriodLenDays()));
		LocalDate predictedStart = insights.getNextStartDate();
		LocalDate predictedEnd = predictedStart == null ? null : predictedStart.plusDays(predictedLen - 1L);

		LocalDate today = LocalDate.now();
		List<List<CalendarDayVm>> weeks = new ArrayList<>();
		List<CalendarDayVm> week = new ArrayList<>(7);

		for (LocalDate d = gridStart; !d.isAfter(gridEnd); d = d.plusDays(1)) {
			boolean inMonth = !d.isBefore(monthStart) && !d.isAfter(monthEnd);
			boolean isToday = d.equals(today);

			// "Actual period" marking: a day is period if it falls in any record's [start, end] range.
			// If end is null (ongoing), we only mark the start day (we don't assume how long it lasts).
			// NOTE: `d` changes in the for-loop, so it is not "effectively final" in Java.
			// Capture it to a final variable so we can use it inside a lambda.
			final LocalDate day = d;
			boolean isPeriod = overlapping.stream().anyMatch(r -> isWithinRecord(day, r));

			// "Predicted period" marking: a soft highlight from predictedStart..predictedEnd.
			boolean isPredicted = predictedStart != null && predictedEnd != null
					&& !d.isBefore(predictedStart) && !d.isAfter(predictedEnd);

			// "Predicted window" marking: slightly different outline for ±3 days around predicted start.
			boolean isPredictedWindow = predictedWindowFrom != null && predictedWindowTo != null
					&& !d.isBefore(predictedWindowFrom) && !d.isAfter(predictedWindowTo);

			week.add(new CalendarDayVm(d, inMonth, isToday, isPeriod, isPredicted, isPredictedWindow));
			if (week.size() == 7) {
				weeks.add(week);
				week = new ArrayList<>(7);
			}
		}
		vm.setWeeks(weeks);
		return vm;
	}

	private static boolean isWithinRecord(LocalDate d, PeriodRecord r) {
		LocalDate s = r.getStartDate();
		if (s == null) return false;
		LocalDate e = r.getEndDate();
		if (e == null) {
			// Ongoing / unknown end: mark start day only.
			return d.equals(s);
		}
		return !d.isBefore(s) && !d.isAfter(e);
	}

	private static LocalDate startOfWeekMonday(LocalDate date) {
		DayOfWeek dow = date.getDayOfWeek(); // MON..SUN
		int shift = (dow.getValue() - DayOfWeek.MONDAY.getValue() + 7) % 7;
		return date.minus(shift, ChronoUnit.DAYS);
	}

	private static LocalDate endOfWeekSunday(LocalDate date) {
		DayOfWeek dow = date.getDayOfWeek();
		int shift = (DayOfWeek.SUNDAY.getValue() - dow.getValue() + 7) % 7;
		return date.plus(shift, ChronoUnit.DAYS);
	}
}

