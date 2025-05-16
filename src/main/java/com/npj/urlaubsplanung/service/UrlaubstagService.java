package com.npj.urlaubsplanung.service;

import com.npj.urlaubsplanung.dto.UrlaubstagDto;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UrlaubstagService {

	public UrlaubstagService() {
	}

	public Iterable<UrlaubstagDto> getUrlaubstag() {
		LocalDate aliceDate = LocalDate.now();
		LocalDate bobDate = LocalDate.now().plusDays(7);
		LocalDate charlieDate = LocalDate.now().minusDays(4);

		return List.of(new UrlaubstagDto("Urlaub - Alice", aliceDate, aliceDate.plusDays(2)),
				new UrlaubstagDto("Urlaub - Bob", bobDate, bobDate.plusDays(3)),
				new UrlaubstagDto("Urlaub - Charlie", charlieDate, charlieDate));
	}
}
