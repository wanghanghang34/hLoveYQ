package com.whh.hloveyq.web.controller;

import com.whh.hloveyq.service.UserService;
import com.whh.hloveyq.web.form.RegisterForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {
	private final UserService userService;

	public AuthController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error,
	                    @RequestParam(value = "logout", required = false) String logout,
	                    @RequestParam(value = "registered", required = false) String registered,
	                    Model model) {
		model.addAttribute("error", error != null);
		model.addAttribute("logout", logout != null);
		model.addAttribute("registered", registered != null);
		return "login";
	}

	/**
	 * Some clients (or reverse proxies) will call "/login/" with a trailing slash.
	 * We treat it the same as "/login" to avoid an extra 302 hop.
	 */
	@GetMapping("/login/")
	public String loginSlash(@RequestParam(value = "error", required = false) String error,
	                         @RequestParam(value = "logout", required = false) String logout,
	                         @RequestParam(value = "registered", required = false) String registered,
	                         Model model) {
		return login(error, logout, registered, model);
	}

	@GetMapping("/register")
	public String register(Model model) {
		if (!model.containsAttribute("form")) {
			model.addAttribute("form", new RegisterForm());
		}
		return "register";
	}

	/**
	 * Some clients (or reverse proxies) will call "/register/" with a trailing slash.
	 * For a non-API web app, it's friendlier to treat it the same as "/register".
	 */
	@GetMapping("/register/")
	public String registerSlash(Model model) {
		return register(model);
	}

	@PostMapping("/register")
	public String doRegister(@Valid @ModelAttribute("form") RegisterForm form,
	                         BindingResult bindingResult,
	                         RedirectAttributes ra) {
		if (bindingResult.hasErrors()) {
			ra.addFlashAttribute("org.springframework.validation.BindingResult.form", bindingResult);
			ra.addFlashAttribute("form", form);
			return "redirect:/register";
		}

		try {
			userService.register(form.getEmail(), form.getPassword(), form.getNickname());
		} catch (IllegalArgumentException ex) {
			ra.addFlashAttribute("registerError", ex.getMessage());
			ra.addFlashAttribute("form", form);
			return "redirect:/register";
		}

		return "redirect:/login?registered";
	}
}

