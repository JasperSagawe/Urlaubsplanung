package com.npj.urlaubsplanung.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
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

import de.focus_shift.jollyday.core.Holiday;

import com.npj.urlaubsplanung.dto.StatusDto;
import com.npj.urlaubsplanung.dto.UrlaubsdatenDto;
import com.npj.urlaubsplanung.dto.UrlaubstagDto;
import com.npj.urlaubsplanung.exception.MaximaleUrlaubstageUeberschrittenException;
import com.npj.urlaubsplanung.exception.AbteilungUrlaubsgrenzeErreichtException;
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
	public String kalender(@AuthenticationPrincipal LoginDetails userDetails, Model model) {
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
	public List<Map<String, Object>> getUrlaubstage(@AuthenticationPrincipal LoginDetails userDetails) {
		List<Map<String, Object>> events;
		Integer abteilungId = userDetails.getMitarbeiter().getMitarbeiterdaten().getAbteilung().getId();
		List<UrlaubstagDto> urlaubstage = (abteilungId != null)
				? urlaubstagService.getUrlaubstageByAbteilung(abteilungId)
				: urlaubstagService.getUrlaubstage();

		events = urlaubstage.stream().filter(u -> !StatusDto.ABGELEHNT.equals(u.getStatus())).map(u -> {
			Map<String, Object> event = new HashMap<>();
			event.put("title", u.getEventName());
			event.put("start", u.getStartdatum().toString());
			event.put("end", u.getEnddatum().plusDays(1).toString());
			event.put("color", StatusDto.BEANTRAGT.equals(u.getStatus()) ? "cyan" : "blue");
			return event;
		}).collect(Collectors.toList());

		int jahr = java.time.LocalDate.now().getYear();
		Set<Holiday> feiertage = urlaubstagService.getFeiertage(jahr);
		for (Holiday feiertag : feiertage) {
			Map<String, Object> event = new HashMap<>();
			event.put("title", feiertag.getDescription());
			event.put("start", feiertag.getDate().toString());
			event.put("end", feiertag.getDate().plusDays(1).toString());
			event.put("color", "red");
			events.add(event);
		}
		return events;
	}

	@DeleteMapping("/urlaubstage/delete/{id}")
	public ResponseEntity<Void> deleteUrlaubstag(@PathVariable Integer id) {
		urlaubstagService.deleteUrlaubsantragById(id);

		return ResponseEntity.ok().build();
	}

	@ResponseBody
	@PostMapping("/urlaubstage/save")
	public ResponseEntity<?> saveUrlaubsantrag(@RequestBody UrlaubstagDto urlaubstagDto,
			@AuthenticationPrincipal LoginDetails userDetails) {
		try {
			UrlaubstagDto gespeichert = urlaubstagService.saveUrlaubsantrag(userDetails.getUsername(), urlaubstagDto);
			return ResponseEntity.ok(gespeichert);

		} catch (MaximaleUrlaubstageUeberschrittenException ex) {
			String message = "DU_KANNST_MAXIMAL_" + ex.getVerbleibendeTage() + "_BEANTRAGEN";
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);

		} catch (AbteilungUrlaubsgrenzeErreichtException ex) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("ZU_VIELE_ABTEILUNGSMITGLIEDER_IM_URLAUB");

		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Unbekannter Fehler: " + ex.getMessage());
		}
	}

}
