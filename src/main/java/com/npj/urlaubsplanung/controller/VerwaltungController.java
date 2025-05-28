package com.npj.urlaubsplanung.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.npj.urlaubsplanung.dto.AbteilungDto;
import com.npj.urlaubsplanung.dto.MitarbeiterDto;
import com.npj.urlaubsplanung.dto.SelectDto;
import com.npj.urlaubsplanung.service.VerwaltungService;

@Controller
@RequestMapping("/verwaltung")
class VerwaltungController {
	private VerwaltungService verwaltungService;

	public VerwaltungController(VerwaltungService verwaltungService) {
		this.verwaltungService = verwaltungService;
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

	@ResponseBody
	@PostMapping("/mitarbeiter/save")
	public ResponseEntity<Void> saveMitarbeiter(@RequestBody MitarbeiterDto mitarbeiterDto) {
		System.out.println("MitarbeiterDto empfangen: " + mitarbeiterDto.getAbteilung().getName()
				+ mitarbeiterDto.getAbteilung().getId());
		verwaltungService.saveMitarbeiter(mitarbeiterDto);
		return ResponseEntity.ok().build();
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

	@ResponseBody
	@PostMapping("/abteilungen/save")
	public ResponseEntity<Void> saveAbteilung(@RequestBody AbteilungDto abteilungDto) {
		verwaltungService.saveAbteilung(abteilungDto);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/abteilungen/remove/{id}")
	public ResponseEntity<Void> removeAbteilungMitarbeiterById(@PathVariable int id) {
		verwaltungService.removeAbteilungMitarbeiterById(id);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/abteilungen/delete/{id}")
	public ResponseEntity<Void> deleteAbteilung(@PathVariable int id) {
		verwaltungService.deleteAbteilungById(id);
		return ResponseEntity.ok().build();
	}

	@ResponseBody
	@GetMapping("/abteilung-select")
	public ResponseEntity<Iterable<SelectDto>> getAbteilungSelect() {
		Iterable<SelectDto> abteilungen = verwaltungService.getAbteilungSelect();
		return ResponseEntity.ok(abteilungen);
	}

	@ResponseBody
	@GetMapping("/rolle-select")
	public ResponseEntity<Iterable<SelectDto>> getRolleSelect() {
		Iterable<SelectDto> rollen = verwaltungService.getRolleSelect();
		return ResponseEntity.ok(rollen);
	}

	@ResponseBody
	@GetMapping("/mitarbeiter-select")
	public ResponseEntity<Iterable<SelectDto>> getMitarbeiterSelect() {
		Iterable<SelectDto> mitarbeiter = verwaltungService.getMitarbeiterSelect();
		return ResponseEntity.ok(mitarbeiter);
	}
}
