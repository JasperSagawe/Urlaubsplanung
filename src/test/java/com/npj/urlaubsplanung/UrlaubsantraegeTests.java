package com.npj.urlaubsplanung;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

import com.npj.urlaubsplanung.dto.StatusDto;
import com.npj.urlaubsplanung.dto.UrlaubstagDto;
import com.npj.urlaubsplanung.exception.AbteilungUrlaubsgrenzeErreichtException;
import com.npj.urlaubsplanung.exception.MaximaleUrlaubstageUeberschrittenException;
import com.npj.urlaubsplanung.model.Abteilung;
import com.npj.urlaubsplanung.model.Mitarbeiter;
import com.npj.urlaubsplanung.model.Mitarbeiterdaten;
import com.npj.urlaubsplanung.model.Urlaubsantrag;
import com.npj.urlaubsplanung.repository.MitarbeiterdatenRepository;
import com.npj.urlaubsplanung.repository.UrlaubsantragRepository;
import com.npj.urlaubsplanung.repository.MitarbeiterRepository;
import com.npj.urlaubsplanung.service.UrlaubstagService;

@ExtendWith(MockitoExtension.class)
class UrlaubsantraegeTests {

	@Mock
	MitarbeiterRepository mitarbeiterRepository;
	@Mock
	MitarbeiterdatenRepository mitarbeiterdatenRepository;
	@Mock
	UrlaubsantragRepository urlaubsantragRepository;

	@InjectMocks
	UrlaubstagService urlaubstagService;

	String email;
	Mitarbeiter mitarbeiter;
	Mitarbeiterdaten daten;
	Abteilung abteilung;
	Urlaubsantrag urlaubsantrag;
	UrlaubstagDto urlaubstagDto;

	@BeforeEach
	void setUp() {
		email = "Alice.Krüger@example.com";
		mitarbeiter = new Mitarbeiter("Alice", "Krüger", email, null);
		abteilung = new Abteilung(null, 100);
		abteilung.setId(1);
		daten = new Mitarbeiterdaten();
		daten.setVerfuegbareUrlaubstage(10);
		daten.setResturlaubVorjahr(1);
		daten.setAbteilung(abteilung);
		mitarbeiter.setMitarbeiterdaten(daten);
		urlaubsantrag = new Urlaubsantrag(mitarbeiter, LocalDate.of(2025, 5, 28), LocalDate.of(2025, 6, 3), 1);
		urlaubsantrag.setId(1);
		urlaubstagDto = new UrlaubstagDto(null, "Alice Krüger", LocalDate.of(2025, 5, 28), LocalDate.of(2025, 6, 3),
				StatusDto.BEANTRAGT);

		Mockito.lenient().when(mitarbeiterRepository.findByEmail(anyString())).thenReturn(Optional.of(mitarbeiter));
		Mockito.lenient().when(mitarbeiterdatenRepository.findByMitarbeiter(any())).thenReturn(daten);
		Mockito.lenient().when(mitarbeiterdatenRepository.countByAbteilungId(anyInt())).thenReturn(1);
		Mockito.lenient().when(urlaubsantragRepository.findAll()).thenReturn(List.of());
		Mockito.lenient().when(urlaubsantragRepository.findById(anyInt())).thenReturn(Optional.of(urlaubsantrag));
	}

	@Test
	void berechneUrlaubstage() {
		// 28.05. (Mittwoch) bis 03.06. (Dienstag) 7 Tage, aber 2 Wochenendtage und 1
		// Feiertag
		int tage = urlaubstagService.berechneTage(urlaubstagDto.getStartdatum(), urlaubstagDto.getEnddatum());
		assertEquals(4, tage);
	}

	@Test
	void zuVieleTeammitgliederImUrlaub() {
		abteilung.setMaxUrlaubProzent(50);

		Mockito.when(mitarbeiterdatenRepository.countByAbteilungId(1)).thenReturn(2);

		// Simuliere, dass schon ein anderer Mitarbeiter im Urlaub ist
		Urlaubsantrag andererUrlaubsantrag = new Urlaubsantrag();
		andererUrlaubsantrag.setMitarbeiter(mitarbeiter);
		andererUrlaubsantrag.setStartDatum(urlaubstagDto.getStartdatum());
		andererUrlaubsantrag.setEndDatum(urlaubstagDto.getStartdatum());
		andererUrlaubsantrag.setStatus(1);

		Mockito.when(urlaubsantragRepository.findAll()).thenReturn(List.of(andererUrlaubsantrag));

		assertThrows(AbteilungUrlaubsgrenzeErreichtException.class, () -> {
			urlaubstagService.saveUrlaubsantrag(email, urlaubstagDto);
		});
	}

	@Test
	void nichtGenuegendUrlaubstage() {
		daten.setVerfuegbareUrlaubstage(0);
		mitarbeiter.setMitarbeiterdaten(daten);

		assertThrows(MaximaleUrlaubstageUeberschrittenException.class, () -> {
			urlaubstagService.saveUrlaubsantrag(email, urlaubstagDto);
		});
	}

	@Test
	void urlaubsantragReduziertVerfuegbareUrlaubstage() {
		urlaubstagService.saveUrlaubsantrag(email, urlaubstagDto);

		assertEquals(6, daten.getVerfuegbareUrlaubstage());
		assertEquals(0, daten.getResturlaubVorjahr());
	}

	@Test
	void urlaubsantragGeloeschtErhoehtVerfuegbareUrlaubstage() {
		urlaubstagService.deleteUrlaubsantragById(1);

		assertEquals(14, daten.getVerfuegbareUrlaubstage());
	}

	@Test
	void urlaubsantragAbgelehntErhoehtVerfuegbareUrlaubstage() {
		urlaubstagService.lehneUrlaubsantragAb(2);

		assertEquals(14, daten.getVerfuegbareUrlaubstage());
	}

	@Test
	void urlaubstagSollteRichtigGespeichertWerden() {
		UrlaubstagDto result = urlaubstagService.saveUrlaubsantrag(email, urlaubstagDto);

		assertEquals("Alice Krüger", result.getEventName());
		assertEquals(urlaubstagDto.getStartdatum(), result.getStartdatum());
		assertEquals(StatusDto.BEANTRAGT, result.getStatus());
	}
}