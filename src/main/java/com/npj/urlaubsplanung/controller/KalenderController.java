package com.npj.urlaubsplanung.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.npj.urlaubsplanung.models.User;
import com.npj.urlaubsplanung.services.UserService;

@Controller
@RequestMapping("/kalender")
class KalenderController {
	private UserService userService;

	public KalenderController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping()
	String kalender(Model model) {
		User user = userService.getUserById();
		model.addAttribute("user", user);
		return "kalender";
	}
}
