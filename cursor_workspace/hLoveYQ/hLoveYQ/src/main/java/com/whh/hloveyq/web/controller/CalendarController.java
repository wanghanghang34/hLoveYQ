package com.whh.hloveyq.web.controller;

import com.whh.hloveyq.security.AuthUserDetails;
import com.whh.hloveyq.service.CalendarService;
import com.whh.hloveyq.web.vm.CalendarVm;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.YearMonth;

@Controller
public class CalendarController {
	private final CalendarService calendarService;

	public CalendarController(CalendarService calendarService) {
		this.calendarService = calendarService;
	}

	@GetMapping("/calendar")
	public String calendar(@AuthenticationPrincipal AuthUserDetails principal,
	                       @RequestParam(value = "year", required = false) Integer year,
	                       @RequestParam(value = "month", required = false) Integer month,
	                       Model model) {
		YearMonth ym = (year == null || month == null)
				? YearMonth.now()
				: YearMonth.of(year, month);

		CalendarVm vm = calendarService.build(principal.getUser(), ym);
		model.addAttribute("vm", vm);
		return "calendar";
	}
}

