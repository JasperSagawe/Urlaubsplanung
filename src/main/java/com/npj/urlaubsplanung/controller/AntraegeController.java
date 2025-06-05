package com.npj.urlaubsplanung.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.npj.urlaubsplanung.dto.UrlaubstagDto;
import com.npj.urlaubsplanung.model.Abteilung;
import com.npj.urlaubsplanung.security.LoginDetails;
import com.npj.urlaubsplanung.service.UrlaubstagService;

@Controller
@RequestMapping("/antraege")
public class AntraegeController {

	private UrlaubstagService urlaubstagService;

	public AntraegeController(UrlaubstagService urlaubstagService) {
		this.urlaubstagService = urlaubstagService;
	}

	@GetMapping
	String kalender(@AuthenticationPrincipal LoginDetails userDetails, Model model) {
		Abteilung abteilung = userDetails.getMitarbeiter().getMitarbeiterdaten().getAbteilung();
		List<UrlaubstagDto> urlaubstage = (abteilung != null)
				? urlaubstagService.getUrlaubstageByAbteilung(abteilung.getId())
				: urlaubstagService.getUrlaubstage();

		model.addAttribute("abteilungsname", abteilung != null ? abteilung.getName() : "Alle Mitarbeiter");
		model.addAttribute("urlaubstage", urlaubstage);
		return "antraege";
	}

	@GetMapping("/tabelle")
	public String antraegeTabelleFragment(@AuthenticationPrincipal LoginDetails userDetails, Model model) {
		Abteilung abteilung = userDetails.getMitarbeiter().getMitarbeiterdaten().getAbteilung();
		List<UrlaubstagDto> urlaubstage = (abteilung != null)
				? urlaubstagService.getUrlaubstageByAbteilung(abteilung.getId())
				: urlaubstagService.getUrlaubstage();

		model.addAttribute("abteilungsname", abteilung != null ? abteilung.getName() : "Alle Mitarbeiter");
		model.addAttribute("urlaubstage", urlaubstage);
		return "common/antraege-tabelle :: antraegeTabelle";
	}

	@ResponseBody
	@PostMapping("/genehmigen/{id}")
	public void genehmigeUrlaub(@PathVariable int id) {
		urlaubstagService.genehmigeUrlaubsantrag(id);
	}

	@ResponseBody
	@PostMapping("/ablehnen/{id}")
	public void lehneUrlaubAb(@PathVariable int id) {
		urlaubstagService.lehneUrlaubsantragAb(id);
	}
}
