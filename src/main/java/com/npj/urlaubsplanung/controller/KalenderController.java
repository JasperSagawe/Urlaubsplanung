package com.npj.urlaubsplanung.controller;

import com.npj.urlaubsplanung.dto.UrlaubstagDto;
import com.npj.urlaubsplanung.service.UrlaubstagService;
import com.npj.urlaubsplanung.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.npj.urlaubsplanung.dto.UserDto;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/kalender")
class KalenderController {

	private UserService userService;
	private UrlaubstagService urlaubstagService;

	public KalenderController(UserService userService, UrlaubstagService urlaubstagService) {
		this.userService = userService;
		this.urlaubstagService = urlaubstagService;
	}

	@GetMapping
	String kalender(Model model) {
		UserDto user = userService.getUserById();
		model.addAttribute("user", user);
		Iterable<UrlaubstagDto> urlaubstage = urlaubstagService.getUrlaubstag();
		model.addAttribute("urlaubstage", urlaubstage);
		return "kalender";
	}

	@ResponseBody
	@GetMapping("/urlaubstage")
	public Iterable<UrlaubstagDto> getUrlaubstage() {
		return urlaubstagService.getUrlaubstag();
	}
}
