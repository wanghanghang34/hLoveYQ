package com.whh.hloveyq.service;

import com.whh.hloveyq.domain.PeriodRecord;
import com.whh.hloveyq.domain.User;
import com.whh.hloveyq.web.vm.AlertVm;
import com.whh.hloveyq.web.vm.InsightsVm;
import com.whh.hloveyq.web.vm.LevelCount;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class InsightsService {
	private final PeriodRecordService periodRecordService;

	public InsightsService(PeriodRecordService periodRecordService) {
		this.periodRecordService = periodRecordService;
	}

	/**
	 * Build a view model for "洞察" (insights).
	 *
	 * <p>Design goal: be helpful but not anxiety-inducing.
	 * All numbers are descriptive statistics only — never medical diagnosis.</p>
	 */
	public InsightsVm build(User user) {
		// We only look at the most recent 12 records to keep insights "recent" and stable.
		List<PeriodRecord> recordsDesc = periodRecordService.recent12(user);
		InsightsVm vm = new InsightsVm();
		vm.setNickname(user.getNickname());
		vm.setAsOfDate(LocalDate.now());

		PeriodRecord latest = recordsDesc.stream()
				.max(Comparator.comparing(PeriodRecord::getStartDate))
				.orElse(null);
		if (latest != null) {
			// Latest record acts as "current context" for the user.
			vm.setLastPeriodStart(latest.getStartDate());
			vm.setLastPeriodEnd(latest.getEndDate());
			vm.setLastPeriodLenDays(periodLenDays(latest));
			vm.setLastPainLevel(latest.getPainLevel());
		}

		List<PeriodRecord> recordsAsc = recordsDesc.stream()
				.sorted(Comparator.comparing(PeriodRecord::getStartDate))
				.toList();

		// Cycle length is computed from adjacent start dates, not from end dates.
		List<Integer> cycleDiffs = computeCycleDiffs(recordsAsc);
		CycleStats cycleStats = calcCycleStats(cycleDiffs);
		vm.setAvgCycleDays(cycleStats.avgDays);
		vm.setCycleMinDays(cycleStats.minDays);
		vm.setCycleMaxDays(cycleStats.maxDays);
		vm.setCycleStdDays(cycleStats.stdDays);
		vm.setLast3CycleDays(cycleStats.last3);
		vm.setPredictionBasis(cycleStats.basis);

		vm.setAvgPeriodLenDays(calcAvgPeriodLen(recordsDesc));
		// Prediction is based on avg cycle and latest start date; we show a ±3 days window.
		applyPrediction(vm);

		vm.setFlowHistogram(calcFlowHistogram(recordsDesc));
		vm.setAvgPainLevel(calcAvgPain(recordsDesc));

		// Alerts are lightweight "heads up" messages based on recent patterns only.
		vm.setAlerts(calcAlerts(recordsAsc, recordsDesc, cycleDiffs));
		return vm;
	}

	private static Integer periodLenDays(PeriodRecord r) {
		if (r.getEndDate() == null || r.getStartDate() == null) return null;
		return (int) (ChronoUnit.DAYS.between(r.getStartDate(), r.getEndDate()) + 1);
	}

	private static Integer calcAvgPeriodLen(List<PeriodRecord> recordsDesc) {
		// Only completed periods (end date exists) can contribute to period-length average.
		List<Integer> lens = recordsDesc.stream()
				.map(InsightsService::periodLenDays)
				.filter(Objects::nonNull)
				.toList();
		if (lens.isEmpty()) return null;
		double avg = lens.stream().mapToInt(i -> i).average().orElse(Double.NaN);
		return (int) Math.round(avg);
	}

	private static Double calcAvgPain(List<PeriodRecord> recordsDesc) {
		// Pain is optional; ignore null values.
		DoubleSummaryStatistics st = recordsDesc.stream()
				.map(PeriodRecord::getPainLevel)
				.filter(Objects::nonNull)
				.mapToDouble(i -> i)
				.summaryStatistics();
		return st.getCount() == 0 ? null : st.getAverage();
	}

	private static List<LevelCount> calcFlowHistogram(List<PeriodRecord> recordsDesc) {
		// Histogram is rendered as 1..5 bars; percent is based on total counts (not max bar).
		Map<Integer, Integer> counts = new HashMap<>();
		int total = 0;
		for (PeriodRecord r : recordsDesc) {
			Integer lvl = r.getFlowLevel();
			if (lvl == null) continue;
			if (lvl < 1 || lvl > 5) continue;
			counts.put(lvl, counts.getOrDefault(lvl, 0) + 1);
			total++;
		}
		if (total == 0) return List.of();
		List<LevelCount> out = new ArrayList<>();
		for (int lvl = 1; lvl <= 5; lvl++) {
			int cnt = counts.getOrDefault(lvl, 0);
			int percent = (int) Math.round((cnt * 100.0) / total);
			out.add(new LevelCount(lvl, cnt, percent));
		}
		return out;
	}

	private static List<Integer> computeCycleDiffs(List<PeriodRecord> recordsAsc) {
		// We compute cycle diffs from distinct start dates:
		//   diff[i] = daysBetween(start[i-1], start[i])
		// This avoids double-counting if the same start date is accidentally saved twice.
		List<LocalDate> starts = recordsAsc.stream()
				.map(PeriodRecord::getStartDate)
				.filter(Objects::nonNull)
				.distinct()
				.sorted()
				.toList();
		List<Integer> diffs = new ArrayList<>();
		for (int i = 1; i < starts.size(); i++) {
			int d = (int) ChronoUnit.DAYS.between(starts.get(i - 1), starts.get(i));
			diffs.add(d);
		}
		return diffs;
	}

	private static CycleStats calcCycleStats(List<Integer> cycleDiffsAll) {
		// Ignore extreme values; they can happen but would distort the simple predictor.
		List<Integer> diffs = cycleDiffsAll.stream()
				.filter(d -> d >= 15 && d <= 60)
				.toList();

		CycleStats st = new CycleStats();
		if (diffs.isEmpty()) {
			// Not enough data: use a gentle default (28 days) and tell the UI the basis.
			st.avgDays = 28;
			st.basis = "default_28";
			return st;
		}

		st.minDays = diffs.stream().min(Integer::compareTo).orElse(null);
		st.maxDays = diffs.stream().max(Integer::compareTo).orElse(null);

		if (diffs.size() >= 3) {
			// Weighted average of last 3 cycles (newest matters more):
			// weights: 1,2,3 → sum=6
			List<Integer> last3 = diffs.subList(diffs.size() - 3, diffs.size());
			st.last3 = new ArrayList<>(last3);
			double weighted = (last3.get(0) * 1 + last3.get(1) * 2 + last3.get(2) * 3) / 6.0;
			st.avgDays = (int) Math.round(weighted);
			st.basis = "weighted_last_3";
		} else {
			// Fallback: simple mean of the available diffs.
			double avg = diffs.stream().mapToInt(i -> i).average().orElse(28);
			st.avgDays = (int) Math.round(avg);
			st.basis = "mean";
			st.last3 = diffs.stream().skip(Math.max(0, diffs.size() - 3L)).collect(Collectors.toList());
		}

		// Sample standard deviation for "stability" display; null when n < 2.
		st.stdDays = stddevSample(diffs);
		return st;
	}

	private static Double stddevSample(List<Integer> values) {
		int n = values.size();
		if (n < 2) return null;
		double mean = values.stream().mapToDouble(i -> i).average().orElse(0);
		double sumSq = 0;
		for (int v : values) {
			double x = v - mean;
			sumSq += x * x;
		}
		return Math.sqrt(sumSq / (n - 1));
	}

	private static void applyPrediction(InsightsVm vm) {
		if (vm.getLastPeriodStart() == null || vm.getAvgCycleDays() == null) return;
		// Predicted next start = last start + average cycle days.
		LocalDate next = vm.getLastPeriodStart().plusDays(vm.getAvgCycleDays());
		vm.setNextStartDate(next);
		// We intentionally show a range to avoid giving a false sense of certainty.
		vm.setNextStartFrom(next.minusDays(3));
		vm.setNextStartTo(next.plusDays(3));
	}

	private static List<AlertVm> calcAlerts(List<PeriodRecord> recordsAsc, List<PeriodRecord> recordsDesc, List<Integer> cycleDiffsAll) {
		List<AlertVm> out = new ArrayList<>();

		// Rule: if the last two valid cycle diffs are both > 45 days, show a gentle heads up.
		List<Integer> diffs = cycleDiffsAll.stream().filter(d -> d >= 15 && d <= 60).toList();
		if (diffs.size() >= 2) {
			int d1 = diffs.get(diffs.size() - 2);
			int d2 = diffs.get(diffs.size() - 1);
			if (d1 > 45 && d2 > 45) {
				out.add(new AlertVm(
						"LONG_CYCLE",
						"最近间隔有点长",
						"压力、作息变化都可能影响节奏。如果你也觉得不安心，可以考虑做一次咨询。",
						"gentle"
				));
			}
		}

		// Rule: high pain (>=7) twice in a row → recommend professional advice.
		List<Integer> recentPain = recordsDesc.stream()
				.map(PeriodRecord::getPainLevel)
				.filter(Objects::nonNull)
				.limit(2)
				.toList();
		if (recentPain.size() == 2 && recentPain.get(0) >= 7 && recentPain.get(1) >= 7) {
			out.add(new AlertVm(
					"HIGH_PAIN",
					"疼痛有点辛苦",
					"痛得厉害真的很累。若持续影响生活，建议咨询专业医生。",
					"recommend"
			));
		}

		// Rule: long bleeding (>=8 days) twice in a row → gentle reminder.
		List<Integer> recentLens = recordsDesc.stream()
				.map(InsightsService::periodLenDays)
				.filter(Objects::nonNull)
				.limit(2)
				.toList();
		if (recentLens.size() == 2 && recentLens.get(0) >= 8 && recentLens.get(1) >= 8) {
			out.add(new AlertVm(
					"LONG_BLEEDING",
					"持续时间偏长",
					"持续时间偏长时，记录会很有帮助，也建议留意身体信号。",
					"gentle"
			));
		}

		return out;
	}

	private static class CycleStats {
		Integer avgDays;
		Integer minDays;
		Integer maxDays;
		Double stdDays;
		List<Integer> last3 = List.of();
		String basis = "mean";
	}
}

