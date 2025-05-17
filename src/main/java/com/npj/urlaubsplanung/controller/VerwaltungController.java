package com.npj.urlaubsplanung.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/verwaltung")
class VerwaltungController {
	@GetMapping()
	String verwaltung() {
		return "verwaltung";
	}
}