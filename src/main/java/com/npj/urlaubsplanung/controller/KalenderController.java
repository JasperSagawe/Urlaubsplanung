package com.npj.urlaubsplanung.controller;

import com.npj.urlaubsplanung.models.Urlaubstag;
import com.npj.urlaubsplanung.models.User;
import com.npj.urlaubsplanung.services.UrlaubstagService;
import com.npj.urlaubsplanung.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
		User user = userService.getUserById();
		model.addAttribute("user", user);
		Iterable<Urlaubstag> urlaubstage = urlaubstagService.getUrlaubstag();
		model.addAttribute("urlaubstage", urlaubstage);
		return "kalender";
	}

	@ResponseBody
	@GetMapping("/urlaubstage")
	public Iterable<Urlaubstag> getUrlaubstage() {
		return urlaubstagService.getUrlaubstag();
	}
}
