package com.npj.urlaubsplanung.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.npj.urlaubsplanung.dto.StatusDto;
import com.npj.urlaubsplanung.dto.UrlaubsdatenDto;
import com.npj.urlaubsplanung.dto.UrlaubstagDto;
import com.npj.urlaubsplanung.exception.AbteilungUrlaubsgrenzeErreichtException;
import com.npj.urlaubsplanung.exception.MaximaleUrlaubstageUeberschrittenException;
import com.npj.urlaubsplanung.model.Abteilung;
import com.npj.urlaubsplanung.model.Mitarbeiter;
import com.npj.urlaubsplanung.model.Mitarbeiterdaten;
import com.npj.urlaubsplanung.model.Urlaubsantrag;
import com.npj.urlaubsplanung.repository.MitarbeiterRepository;
import com.npj.urlaubsplanung.repository.MitarbeiterdatenRepository;
import com.npj.urlaubsplanung.repository.UrlaubsantragRepository;

import static de.focus_shift.jollyday.core.HolidayCalendar.GERMANY;
import de.focus_shift.jollyday.core.Holiday;
import de.focus_shift.jollyday.core.HolidayManager;
import de.focus_shift.jollyday.core.ManagerParameters;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UrlaubstagService {

	private MitarbeiterRepository mitarbeiterRepository;
	private MitarbeiterdatenRepository mitarbeiterdatenRepository;
	private UrlaubsantragRepository urlaubsantragRepository;

	public UrlaubstagService(MitarbeiterRepository mitarbeiterRepository,
			MitarbeiterdatenRepository mitarbeiterdatenRepository, UrlaubsantragRepository urlaubsantragRepository) {
		this.mitarbeiterRepository = mitarbeiterRepository;
		this.mitarbeiterdatenRepository = mitarbeiterdatenRepository;
		this.urlaubsantragRepository = urlaubsantragRepository;
	}

	public List<UrlaubstagDto> getUrlaubstage() {
		return mapUrlaubsantraegeToDto(this.urlaubsantragRepository.findAll());
	}

	public List<UrlaubstagDto> getUrlaubstageByAbteilung(int id) {
		return mapUrlaubsantraegeToDto(this.urlaubsantragRepository.findAll().stream().filter(antrag -> {
			Mitarbeiter mitarbeiter = antrag.getMitarbeiter();
			Abteilung abteilung = mitarbeiter.getMitarbeiterdaten().getAbteilung();

			return abteilung != null && abteilung.getId() == id;
		}).toList());
	}

	private List<UrlaubstagDto> mapUrlaubsantraegeToDto(List<Urlaubsantrag> urlaubsantraege) {
		LocalDate heute = LocalDate.now();
		return urlaubsantraege.stream().map(antrag -> {
			StatusDto status;

			// 1 = Status Genehmigt
			if (antrag.getStatus() == 1 && antrag.getStartDatum().isBefore(heute)) {
				status = StatusDto.GENOMMEN;
			} else {
				status = switch (antrag.getStatus()) {
				case 0 -> StatusDto.BEANTRAGT;
				case 1 -> StatusDto.GENEHMIGT;
				case 2 -> StatusDto.ABGELEHNT;
				default -> StatusDto.BEANTRAGT;
				};
			}
			Mitarbeiter m = antrag.getMitarbeiter();
			String mitarbeiterName = m.getVorname() + " " + m.getNachname();

			return new UrlaubstagDto(antrag.getId(), mitarbeiterName, antrag.getStartDatum(), antrag.getEndDatum(),
					status);
		}).sorted(Comparator.comparing(UrlaubstagDto::getStartdatum)).toList();
	}

	public UrlaubstagDto saveUrlaubsantrag(String email, UrlaubstagDto urlaubstagDto) {
		Mitarbeiter mitarbeiter = this.mitarbeiterRepository.findByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException("Mitarbeiter nicht gefunden"));

		Mitarbeiterdaten mitarbeiterdaten = this.mitarbeiterdatenRepository.findByMitarbeiter(mitarbeiter);

		int tage = berechneTage(urlaubstagDto.getStartdatum(), urlaubstagDto.getEnddatum());
		int verfuegbareUrlaubstage = mitarbeiterdaten.getVerfuegbareUrlaubstage();

		if (tage > verfuegbareUrlaubstage) {
			throw new MaximaleUrlaubstageUeberschrittenException(verfuegbareUrlaubstage);
		}

		// 0 = Status Beantragt
		Urlaubsantrag urlaubsantrag = new Urlaubsantrag(mitarbeiter, urlaubstagDto.getStartdatum(),
				urlaubstagDto.getEnddatum(), 0);

		Abteilung abteilung = mitarbeiter.getMitarbeiterdaten().getAbteilung();
		List<LocalDate> urlaubstage = urlaubstagDto.getStartdatum().datesUntil(urlaubstagDto.getEnddatum().plusDays(1))
				.toList();
		int abteilungMitglieder = mitarbeiterdatenRepository.countByAbteilungId(abteilung.getId());
		int abteilungMaxUrlaubProzent = abteilung.getMaxUrlaubProzent();
		double maxUrlauber = abteilungMitglieder * (abteilungMaxUrlaubProzent / 100.0);

		for (LocalDate tag : urlaubstage) {
			long bereitsImUrlaub = urlaubsantragRepository.findAll().stream().filter(antrag -> {
				Mitarbeiter m = antrag.getMitarbeiter();
				Abteilung a = m.getMitarbeiterdaten().getAbteilung();
				return a != null && a.getId() == abteilung.getId() && !antrag.getStartDatum().isAfter(tag)
						&& !antrag.getEndDatum().isBefore(tag) && (antrag.getStatus() == 0 || antrag.getStatus() == 1);
			}).count();

			if (bereitsImUrlaub >= maxUrlauber) {
				throw new AbteilungUrlaubsgrenzeErreichtException();
			}
		}

		int resturlaubVorjahr = mitarbeiterdaten.getResturlaubVorjahr();
		if (resturlaubVorjahr > 0) {
			if (resturlaubVorjahr >= tage) {
				mitarbeiterdaten.setResturlaubVorjahr(resturlaubVorjahr - tage);
			} else {
				mitarbeiterdaten.setResturlaubVorjahr(0);
			}
		}

		mitarbeiterdaten.setVerfuegbareUrlaubstage(verfuegbareUrlaubstage - tage);
		this.mitarbeiterdatenRepository.save(mitarbeiterdaten);
		this.urlaubsantragRepository.save(urlaubsantrag);

		String mitarbeiterName = mitarbeiter.getVorname() + " " + mitarbeiter.getNachname();

		return new UrlaubstagDto(urlaubstagDto.getId(), mitarbeiterName, urlaubstagDto.getStartdatum(),
				urlaubstagDto.getEnddatum(), StatusDto.BEANTRAGT);
	}

	public void deleteUrlaubsantragById(Integer id) {
		Urlaubsantrag urlaubsantrag = urlaubsantragRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Urlaubsantrag nicht gefunden"));
		int status = urlaubsantrag.getStatus();

		// Nur wenn Status BEANTRAGT (0) oder GENEHMIGT (1)
		if (status == 0 || status == 1) {
			Mitarbeiter mitarbeiter = urlaubsantrag.getMitarbeiter();
			Mitarbeiterdaten mitarbeiterdaten = mitarbeiterdatenRepository.findByMitarbeiter(mitarbeiter);

			int tage = berechneTage(urlaubsantrag.getStartDatum(), urlaubsantrag.getEndDatum());
			mitarbeiterdaten.setVerfuegbareUrlaubstage(mitarbeiterdaten.getVerfuegbareUrlaubstage() + tage);
			mitarbeiterdatenRepository.save(mitarbeiterdaten);
		}

		urlaubsantragRepository.deleteById(id);
	}

	public UrlaubsdatenDto getUrlaubsdaten(String email) {
		Mitarbeiter mitarbeiter = mitarbeiterRepository.findByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException("Mitarbeiter nicht gefunden"));
		Mitarbeiterdaten mitarbeiterdaten = aktualisiereMitarbeiterdaten(
				mitarbeiterdatenRepository.findByMitarbeiter(mitarbeiter));
		List<Urlaubsantrag> urlaubsantraege = urlaubsantragRepository.findByMitarbeiter(mitarbeiter);

		LocalDate heute = LocalDate.now();
		AtomicInteger beantragt = new AtomicInteger();
		AtomicInteger genommen = new AtomicInteger();

		List<UrlaubstagDto> urlaubstage = urlaubsantraege.stream().map(antrag -> {
			int tage = berechneTage(antrag.getStartDatum(), antrag.getEndDatum());
			StatusDto status;

			if (antrag.getStatus() == 1 && antrag.getStartDatum().isBefore(heute)) {
				status = StatusDto.GENOMMEN;
				genommen.addAndGet(tage);
			} else {
				status = switch (antrag.getStatus()) {
				case 0 -> StatusDto.BEANTRAGT;
				case 1 -> StatusDto.GENEHMIGT;
				case 2 -> StatusDto.ABGELEHNT;
				default -> StatusDto.BEANTRAGT;
				};

				if (antrag.getStatus() == 0 || antrag.getStatus() == 1)
					beantragt.addAndGet(tage);
			}

			String mitarbeiterName = mitarbeiter.getVorname() + " " + mitarbeiter.getNachname();

			return new UrlaubstagDto(antrag.getId(), mitarbeiterName, antrag.getStartDatum(), antrag.getEndDatum(),
					status);
		}).sorted(Comparator.comparing(UrlaubstagDto::getStartdatum)).toList();

		return new UrlaubsdatenDto(mitarbeiterdaten.getResturlaubVorjahr(),
				mitarbeiterdaten.getVerfuegbareUrlaubstage(), beantragt.get(), genommen.get(), urlaubstage);
	}

	public Mitarbeiterdaten aktualisiereMitarbeiterdaten(Mitarbeiterdaten daten) {
		int aktuellesJahr = LocalDate.now().getYear();
		boolean aktualisiert = false;

		while (daten.getAktuellesJahr() < aktuellesJahr) {
			int rest = daten.getVerfuegbareUrlaubstage();
			daten.setResturlaubVorjahr(rest);
			daten.setVerfuegbareUrlaubstage(daten.getUrlaubstageProJahr() + rest);
			daten.setAktuellesJahr(daten.getAktuellesJahr() + 1);
			aktualisiert = true;
		}

		if (aktualisiert) {
			mitarbeiterdatenRepository.save(daten);
		}

		return daten;
	}

	public void genehmigeUrlaubsantrag(int id) {
		Urlaubsantrag urlaubsantrag = urlaubsantragRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Urlaubsantrag nicht gefunden"));
		urlaubsantrag.setStatus(1);
		urlaubsantragRepository.save(urlaubsantrag);

	}

	public void lehneUrlaubsantragAb(int id) {
		Urlaubsantrag urlaubsantrag = urlaubsantragRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Urlaubsantrag nicht gefunden"));
		Mitarbeiter mitarbeiter = urlaubsantrag.getMitarbeiter();
		Mitarbeiterdaten mitarbeiterdaten = mitarbeiterdatenRepository.findByMitarbeiter(mitarbeiter);

		urlaubsantrag.setStatus(2);

		int tage = berechneTage(urlaubsantrag.getStartDatum(), urlaubsantrag.getEndDatum());
		mitarbeiterdaten.setVerfuegbareUrlaubstage(mitarbeiterdaten.getVerfuegbareUrlaubstage() + tage);

		urlaubsantragRepository.save(urlaubsantrag);
		mitarbeiterdatenRepository.save(mitarbeiterdaten);
	}

	public int berechneTage(LocalDate start, LocalDate end) {
		Set<Holiday> feiertage = getFeiertage(start, end);
		int tage = 0;
		for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
			LocalDate day = date;
			DayOfWeek dayOfWeek = date.getDayOfWeek();
			boolean isWeekend = (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY);
			boolean isFeiertag = feiertage.stream().anyMatch(f -> f.getDate().equals(day));
			if (!isWeekend && !isFeiertag) {
				tage++;
			}
		}
		return tage;
	}

	public Set<Holiday> getFeiertage(int year) {
		HolidayManager manager = HolidayManager.getInstance(ManagerParameters.create(GERMANY));
		return manager.getHolidays(Year.of(year));
	}

	public Set<Holiday> getFeiertage(LocalDate start, LocalDate end) {
		HolidayManager manager = HolidayManager.getInstance(ManagerParameters.create(GERMANY));
		return manager.getHolidays(start, end);
	}
}
