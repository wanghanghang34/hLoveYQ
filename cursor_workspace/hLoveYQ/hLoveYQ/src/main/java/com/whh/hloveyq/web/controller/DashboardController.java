package com.whh.hloveyq.web.controller;

import com.whh.hloveyq.domain.User;
import com.whh.hloveyq.security.AuthUserDetails;
import com.whh.hloveyq.service.InsightsService;
import com.whh.hloveyq.web.vm.InsightsVm;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
	private final InsightsService insightsService;

	public DashboardController(InsightsService insightsService) {
		this.insightsService = insightsService;
	}

	@GetMapping("/")
	public String dashboard(@AuthenticationPrincipal AuthUserDetails principal, Model model) {
		User user = principal.getUser();
		InsightsVm vm = insightsService.build(user);
		model.addAttribute("vm", vm);
		return "dashboard";
	}
}

