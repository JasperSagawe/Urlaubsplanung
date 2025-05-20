package com.npj.urlaubsplanung.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.npj.urlaubsplanung.dto.UserDto;
import com.npj.urlaubsplanung.service.UserService;

@Controller
@RequestMapping("/verwaltung")
class VerwaltungController {
	private UserService userService;

	public VerwaltungController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping()
	String verwaltung() {
		return "verwaltung";
	}

	@GetMapping("/mitarbeiter")
	public String mitarbeiter(Model model) {
		Iterable<UserDto> user = userService.getUser();
		model.addAttribute("user", user);
		return "mitarbeiter";
	}
}
