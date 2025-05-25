package com.npj.urlaubsplanung.service;

import static de.focus_shift.jollyday.core.HolidayCalendar.GERMANY;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.npj.urlaubsplanung.dto.StatusDto;
import com.npj.urlaubsplanung.dto.UrlaubsdatenDto;
import com.npj.urlaubsplanung.dto.UrlaubstagDto;
import com.npj.urlaubsplanung.model.Mitarbeiter;
import com.npj.urlaubsplanung.model.Mitarbeiterdaten;
import com.npj.urlaubsplanung.model.Urlaubsantrag;
import com.npj.urlaubsplanung.repository.MitarbeiterRepository;
import com.npj.urlaubsplanung.repository.MitarbeiterdatenRepository;
import com.npj.urlaubsplanung.repository.UrlaubsantragRepository;

import de.focus_shift.jollyday.core.Holiday;
import de.focus_shift.jollyday.core.HolidayManager;
import de.focus_shift.jollyday.core.ManagerParameters;

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

	public void deleteUrlaubstagById(Integer id) {
		Optional<Urlaubsantrag> urlaubsantragOpt = urlaubsantragRepository.findById(id);
		if (urlaubsantragOpt.isPresent()) {
			Urlaubsantrag urlaubsantrag = urlaubsantragOpt.get();
			int status = urlaubsantrag.getStatus();
			// Nur wenn Status BEANTRAGT (0) oder GENEHMIGT (1)
			if (status == 0 || status == 1) {
				Mitarbeiter mitarbeiter = urlaubsantrag.getMitarbeiter();
				Mitarbeiterdaten mitarbeiterdaten = mitarbeiterdatenRepository.findByMitarbeiter(mitarbeiter);
				if (mitarbeiterdaten != null) {
					long tage = berechneTage(urlaubsantrag.getStartDatum(), urlaubsantrag.getEndDatum());
					mitarbeiterdaten
							.setVerfuegbareUrlaubstage(mitarbeiterdaten.getVerfuegbareUrlaubstage() + (int) tage);
					mitarbeiterdatenRepository.save(mitarbeiterdaten);
				}
			}
			urlaubsantragRepository.deleteById(id);
		}
	}

	public List<UrlaubstagDto> getUrlaubstage() {
		List<Urlaubsantrag> urlaubsantraege = this.urlaubsantragRepository.findAll();

		return urlaubsantraege.stream().map(antrag -> {
			StatusDto status;
			status = switch (antrag.getStatus()) {
			case 0 -> StatusDto.BEANTRAGT;
			case 1 -> StatusDto.GENEHMIGT;
			case 2 -> StatusDto.ABGELEHNT;
			default -> StatusDto.BEANTRAGT;
			};
			Mitarbeiter mitarbeiter = antrag.getMitarbeiter();
			String mitarbeiterName = "Urlaub - " + mitarbeiter.getVorname() + " " + mitarbeiter.getNachname();
			return new UrlaubstagDto(antrag.getId(), mitarbeiterName, antrag.getStartDatum(), antrag.getEndDatum(),
					status);
		}).toList();
	}

	public UrlaubstagDto saveUrlaubsantrag(String email, UrlaubstagDto urlaubstagDto) {
		Optional<Mitarbeiter> mitarbeiterOpt = this.mitarbeiterRepository.findByEmail(email);

		if (mitarbeiterOpt.isPresent()) {
			Mitarbeiter mitarbeiter = mitarbeiterOpt.get();

			long tage = berechneTage(urlaubstagDto.getStartDate(), urlaubstagDto.getEndDate());

			Mitarbeiterdaten mitarbeiterdaten = this.mitarbeiterdatenRepository.findByMitarbeiter(mitarbeiter);
			if (mitarbeiterdaten != null) {
				int verfuegbareUrlaubstage = mitarbeiterdaten.getVerfuegbareUrlaubstage();
				mitarbeiterdaten.setVerfuegbareUrlaubstage(verfuegbareUrlaubstage - (int) tage);
				this.mitarbeiterdatenRepository.save(mitarbeiterdaten);
			}

			Urlaubsantrag urlaubsantrag = new Urlaubsantrag(mitarbeiter, urlaubstagDto.getStartDate(),
					urlaubstagDto.getEndDate(), 0, null);
			this.urlaubsantragRepository.save(urlaubsantrag);

			String mitarbeiterName = "Urlaub - " + mitarbeiter.getVorname() + " " + mitarbeiter.getNachname();

			return new UrlaubstagDto(urlaubstagDto.getId(), mitarbeiterName, urlaubstagDto.getStartDate(),
					urlaubstagDto.getEndDate(), null);
		}

		return null;
	}

	public UrlaubsdatenDto getUrlaubsdaten(String email) {
		Optional<Mitarbeiter> mitarbeiterOpt = mitarbeiterRepository.findByEmail(email);
		if (mitarbeiterOpt.isEmpty())
			return null;

		Mitarbeiter mitarbeiter = mitarbeiterOpt.get();
		Mitarbeiterdaten mitarbeiterdaten = aktualisiereMitarbeiterdaten(
				mitarbeiterdatenRepository.findByMitarbeiter(mitarbeiter));
		List<Urlaubsantrag> urlaubsantraege = urlaubsantragRepository.findByMitarbeiter(mitarbeiter);

		AtomicInteger beantragt = new AtomicInteger();
		AtomicInteger genommen = new AtomicInteger();
		LocalDate heute = LocalDate.now();

		List<UrlaubstagDto> urlaubstage = urlaubsantraege.stream().map(antrag -> {
			long tage = berechneTage(antrag.getStartDatum(), antrag.getEndDatum());
			StatusDto status;
			if (antrag.getStatus() == 1 && antrag.getStartDatum().isBefore(heute)) {
				status = StatusDto.GENOMMEN;
				genommen.addAndGet((int) tage);
			} else {
				status = switch (antrag.getStatus()) {
				case 0 -> StatusDto.BEANTRAGT;
				case 1 -> StatusDto.GENEHMIGT;
				case 2 -> StatusDto.ABGELEHNT;
				default -> StatusDto.BEANTRAGT;
				};
				if (antrag.getStatus() == 0 || antrag.getStatus() == 1)
					beantragt.addAndGet((int) tage);
			}
			return new UrlaubstagDto(antrag.getId(), "Urlaub", antrag.getStartDatum(), antrag.getEndDatum(), status);
		}).sorted(Comparator.comparing(UrlaubstagDto::getStartDate)).toList();

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

	public long berechneTage(LocalDate start, LocalDate end) {
		Set<Holiday> feiertage = getFeiertage(start, end);
		long tage = 0;
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
