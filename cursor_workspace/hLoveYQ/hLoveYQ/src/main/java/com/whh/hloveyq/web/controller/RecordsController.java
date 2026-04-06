package com.whh.hloveyq.web.controller;

import com.whh.hloveyq.domain.PeriodRecord;
import com.whh.hloveyq.domain.User;
import com.whh.hloveyq.security.AuthUserDetails;
import com.whh.hloveyq.service.PeriodRecordService;
import com.whh.hloveyq.web.form.PeriodRecordForm;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class RecordsController {
	private final PeriodRecordService periodRecordService;

	public RecordsController(PeriodRecordService periodRecordService) {
		this.periodRecordService = periodRecordService;
	}

	@GetMapping("/records")
	public String list(@AuthenticationPrincipal AuthUserDetails principal, Model model) {
		User user = principal.getUser();
		List<PeriodRecord> records = periodRecordService.recent12(user);
		model.addAttribute("records", records);
		model.addAttribute("nickname", user.getNickname());
		return "records";
	}

	@GetMapping("/records/new")
	public String createForm(Model model) {
		if (!model.containsAttribute("form")) {
			model.addAttribute("form", new PeriodRecordForm());
		}
		return "record_form";
	}

	@PostMapping("/records")
	public String create(@AuthenticationPrincipal AuthUserDetails principal,
	                     @Valid @ModelAttribute("form") PeriodRecordForm form,
	                     BindingResult bindingResult,
	                     RedirectAttributes ra) {
		if (bindingResult.hasErrors()) {
			ra.addFlashAttribute("org.springframework.validation.BindingResult.form", bindingResult);
			ra.addFlashAttribute("form", form);
			return "redirect:/records/new";
		}

		try {
			periodRecordService.create(principal.getUser(), form);
		} catch (IllegalArgumentException ex) {
			ra.addFlashAttribute("recordError", ex.getMessage());
			ra.addFlashAttribute("form", form);
			return "redirect:/records/new";
		}

		return "redirect:/records";
	}
}

