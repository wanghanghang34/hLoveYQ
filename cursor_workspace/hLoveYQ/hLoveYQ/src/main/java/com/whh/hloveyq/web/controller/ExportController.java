package com.whh.hloveyq.web.controller;

import com.whh.hloveyq.domain.PeriodRecord;
import com.whh.hloveyq.security.AuthUserDetails;
import com.whh.hloveyq.service.PeriodRecordService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class ExportController {
	private final PeriodRecordService periodRecordService;

	public ExportController(PeriodRecordService periodRecordService) {
		this.periodRecordService = periodRecordService;
	}

	@GetMapping("/export.csv")
	public ResponseEntity<byte[]> exportCsv(@AuthenticationPrincipal AuthUserDetails principal) {
		List<PeriodRecord> records = periodRecordService.recent12(principal.getUser());
		StringBuilder sb = new StringBuilder();
		sb.append("start_date,end_date,flow_level,pain_level,note\n");
		DateTimeFormatter df = DateTimeFormatter.ISO_LOCAL_DATE;
		for (PeriodRecord r : records) {
			sb.append(csv(df.format(r.getStartDate()))).append(",");
			sb.append(csv(r.getEndDate() == null ? "" : df.format(r.getEndDate()))).append(",");
			sb.append(csv(r.getFlowLevel() == null ? "" : r.getFlowLevel().toString())).append(",");
			sb.append(csv(r.getPainLevel() == null ? "" : r.getPainLevel().toString())).append(",");
			sb.append(csv(r.getNote() == null ? "" : r.getNote())).append("\n");
		}
		byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
		return ResponseEntity.ok()
				.contentType(new MediaType("text", "csv", StandardCharsets.UTF_8))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"huniuzhouquan.csv\"")
				.body(bytes);
	}

	private static String csv(String s) {
		String v = s == null ? "" : s;
		boolean needQuote = v.contains(",") || v.contains("\"") || v.contains("\n") || v.contains("\r");
		String escaped = v.replace("\"", "\"\"");
		return needQuote ? ("\"" + escaped + "\"") : escaped;
	}
}

