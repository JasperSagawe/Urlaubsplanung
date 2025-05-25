package com.npj.urlaubsplanung.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;

import com.npj.urlaubsplanung.dto.UrlaubsdatenDto;
import com.npj.urlaubsplanung.dto.UrlaubstagDto;
import com.npj.urlaubsplanung.service.UrlaubstagService;
import com.npj.urlaubsplanung.security.LoginDetails;

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
		return "common/kalender-tabellen :: kalenderTabellen";
	}

	@ResponseBody
	@GetMapping("/urlaubstage")
	public List<Map<String, Object>> getUrlaubstage() {
		return urlaubstagService.getUrlaubstage().stream().map(u -> {
			Map<String, Object> event = new HashMap<>();
			event.put("title", u.getEventName());
			event.put("start", u.getStartDate().toString());
			event.put("end", u.getEndDate().plusDays(1).toString());
			return event;
		}).collect(Collectors.toList());
	}

	@DeleteMapping("/urlaubstage/delete/{id}")
	public ResponseEntity<Void> deleteUrlaubstag(@PathVariable Integer id) {
		urlaubstagService.deleteUrlaubstagById(id);
		return ResponseEntity.ok().build();
	}

	@ResponseBody
	@PostMapping("/urlaubstage/save")
	public UrlaubstagDto saveUrlaubsantrag(@RequestBody UrlaubstagDto urlaubstagDto,
			@AuthenticationPrincipal LoginDetails userDetails) {
		return urlaubstagService.saveUrlaubsantrag(userDetails.getUsername(), urlaubstagDto);
	}

}
