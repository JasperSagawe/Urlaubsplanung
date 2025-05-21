package com.npj.urlaubsplanung.controller;

import com.npj.urlaubsplanung.dto.UrlaubsdatenDto;
import com.npj.urlaubsplanung.dto.UrlaubstagDto;
import com.npj.urlaubsplanung.service.UrlaubstagService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.npj.urlaubsplanung.security.LoginDetails;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/kalender")
class KalenderController {

	private UrlaubstagService urlaubstagService;

	public KalenderController(UrlaubstagService urlaubstagService) {
		this.urlaubstagService = urlaubstagService;
	}

	@GetMapping
	String kalender(@AuthenticationPrincipal LoginDetails userDetails, Model model) {
		UrlaubsdatenDto urlaubstage = urlaubstagService.getUrlaubsdaten(userDetails.getUsername());
		model.addAttribute("user", urlaubstage);
		model.addAttribute("urlaubstage", urlaubstage.getUrlaubstage());
		return "kalender";
	}

	@GetMapping("/urlaub-tabelle")
	public String urlaubTabelleFragment(@AuthenticationPrincipal LoginDetails userDetails, Model model) {
		UrlaubsdatenDto urlaubstage = urlaubstagService.getUrlaubsdaten(userDetails.getUsername());
		model.addAttribute("user", urlaubstage);
		model.addAttribute("urlaubstage", urlaubstage.getUrlaubstage());
    	return "common/urlaubstabelle :: urlaubstabelle";
}

	@ResponseBody
	@GetMapping("/urlaubstage")
	public Iterable<UrlaubstagDto> getUrlaubstage() {
		return urlaubstagService.getUrlaubstage();
	}

	@PostMapping("/urlaubstage/delete/{id}")
	public String deleteUrlaubstag(@PathVariable Integer id) {
		urlaubstagService.deleteUrlaubstagById(id);
		return "redirect:/kalender";
	}

	@ResponseBody
	@PostMapping("/urlaubstage/save")
	public UrlaubstagDto saveUrlaubsantrag(@RequestBody UrlaubstagDto urlaubstagDto,
			@AuthenticationPrincipal LoginDetails userDetails) {
		return urlaubstagService.saveUrlaubsantrag(userDetails.getUsername(), urlaubstagDto);
	}

}
