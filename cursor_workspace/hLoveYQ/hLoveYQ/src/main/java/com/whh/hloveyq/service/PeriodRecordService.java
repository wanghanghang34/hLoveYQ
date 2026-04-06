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

@Service
public class PeriodRecordService {
	private final PeriodRecordRepository periodRecordRepository;

	public PeriodRecordService(PeriodRecordRepository periodRecordRepository) {
		this.periodRecordRepository = periodRecordRepository;
	}

	public List<PeriodRecord> recent12(User user) {
		return periodRecordRepository.findTop12ByUserOrderByStartDateDesc(user);
	}

	public PeriodRecord latest(User user) {
		return periodRecordRepository.findTop1ByUserOrderByStartDateDesc(user).orElse(null);
	}

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

