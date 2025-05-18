package com.npj.urlaubsplanung.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.npj.urlaubsplanung.dto.UserDto;
import com.npj.urlaubsplanung.repository.MitarbeiterRepository;

@Service
public class UserService {

	private MitarbeiterRepository mitarbeiterRepository;

	public UserService(MitarbeiterRepository mitarbeiterRepository) {
		this.mitarbeiterRepository = mitarbeiterRepository;
	}

	public Iterable<UserDto> getUser() {
		return List.of(new UserDto("Alice", "Krüger", 24, 6), new UserDto("Bob", "Baumeister", 20, 10),
				new UserDto("Charlie", "Brown", 12, 18));
	}

	public UserDto getUserByEMail(String email) {
		return this.mitarbeiterRepository.findByEmail(email)
				.map(mitarbeiter -> new UserDto(mitarbeiter.getVorname(), mitarbeiter.getNachname(), 13, 4))
				.orElse(null);
	}
}
