package com.npj.urlaubsplanung.services;

import com.npj.urlaubsplanung.models.Urlaubstag;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UrlaubstagService {

	public UrlaubstagService() {
	}

	public Iterable<Urlaubstag> getUrlaubstag() {
		LocalDate aliceDate = LocalDate.now();
		LocalDate bobDate = LocalDate.now().plusDays(7);
		LocalDate charlieDate = LocalDate.now().minusDays(4);

		return List.of(new Urlaubstag("Urlaub - Alice", aliceDate, aliceDate.plusDays(2)),
				new Urlaubstag("Urlaub - Bob", bobDate, bobDate.plusDays(3)),
				new Urlaubstag("Urlaub - Charlie", charlieDate, charlieDate));
	}
}
