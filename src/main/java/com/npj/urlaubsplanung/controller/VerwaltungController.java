package com.npj.urlaubsplanung.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.npj.urlaubsplanung.dto.AbteilungDto;
import com.npj.urlaubsplanung.dto.MitarbeiterDto;
import com.npj.urlaubsplanung.service.VerwaltungService;

@Controller
@RequestMapping("/verwaltung")
class VerwaltungController {
	private VerwaltungService verwaltungService;

	public VerwaltungController(VerwaltungService verwaltungService) {
		this.verwaltungService = verwaltungService;
	}

	@GetMapping()
	String verwaltung() {
		return "verwaltung";
	}
		
		
	@GetMapping("/mitarbeiter")
	public String mitarbeiter(Model model) {		
		Iterable<MitarbeiterDto> mitarbeiter = verwaltungService.getMitarbeiter();
		model.addAttribute("mitarbeiter", mitarbeiter);
		return "mitarbeiter";
	}

	@GetMapping("/mitarbeiter-tabelle")
	public String mitarbeiterTabelleFragment(Model model) {
		Iterable<MitarbeiterDto> mitarbeiter = verwaltungService.getMitarbeiter();
		model.addAttribute("mitarbeiter", mitarbeiter);
		return "common/mitarbeiter-tabelle :: mitarbeiterTabelle";
	}

	@DeleteMapping("/mitarbeiter/delete/{id}")
	public ResponseEntity<Void> deleteMitarbeiter(@PathVariable int id) {
		verwaltungService.deleteMitarbeiterById(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/abteilungen")
	public String abteilungen(Model model) {		
		Iterable<AbteilungDto> abteilungen = verwaltungService.getAbteilungen();
		model.addAttribute("abteilungen", abteilungen);
		return "abteilungen";
	}

	@GetMapping("/abteilungen-tabelle")
	public String abteilungenTabelleFragment(Model model) {
		Iterable<AbteilungDto> abteilungen = verwaltungService.getAbteilungen();
		model.addAttribute("abteilungen", abteilungen);
		return "common/abteilungen-tabelle :: abteilungenTabelle";
	}
}
