package com.npj.urlaubsplanung.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.npj.urlaubsplanung.dto.UrlaubstagDto;
import com.npj.urlaubsplanung.model.Team;
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
		Team team = userDetails.getMitarbeiter().getMitarbeiterdaten().getTeam();
		List<UrlaubstagDto> urlaubstage = urlaubstagService.getUrlaubstageByTeam(team.getId());

		model.addAttribute("teamname", team.getName());
		model.addAttribute("urlaubstage", urlaubstage);
		return "antraege";
	}

	@GetMapping("/tabelle")
	public String antraegeTabelleFragment(@AuthenticationPrincipal LoginDetails userDetails, Model model) {
		Team team = userDetails.getMitarbeiter().getMitarbeiterdaten().getTeam();
		List<UrlaubstagDto> urlaubstage = urlaubstagService.getUrlaubstageByTeam(team.getId());
		model.addAttribute("teamname", team.getName());
		model.addAttribute("urlaubstage", urlaubstage);
		return "common/antraege-tabelle :: antraegeTabelle";
	}

	@PostMapping("/genehmigen/{id}")
	@ResponseBody
	public void genehmigeUrlaub(@PathVariable Long id) {
		urlaubstagService.genehmigeUrlaubstag(id);
	}

	@PostMapping("/ablehnen/{id}")
	@ResponseBody
	public void lehneUrlaubAb(@PathVariable Long id) {
		urlaubstagService.lehneUrlaubstagAb(id);
	}
}
