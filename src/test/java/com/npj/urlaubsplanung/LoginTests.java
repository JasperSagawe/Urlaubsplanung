package com.npj.urlaubsplanung;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.npj.urlaubsplanung.model.Mitarbeiter;
import com.npj.urlaubsplanung.repository.MitarbeiterRepository;
import com.npj.urlaubsplanung.security.LoginDetails;
import com.npj.urlaubsplanung.service.LoginDetailsService;

@ExtendWith(MockitoExtension.class)
class LoginTests {

	@Mock
	MitarbeiterRepository mitarbeiterRepository;

	@InjectMocks
	LoginDetailsService loginDetailsService;

	String email;
	Mitarbeiter mitarbeiter;

	@BeforeEach
	void setUp() {
		email = "Alice.Krüger@example.com";
		mitarbeiter = new Mitarbeiter("Alice", "Krüger", email, null);
	}

	@Test
	void loginErfolgreich() {
		LoginDetails user = new LoginDetails(mitarbeiter);

		Mockito.when(mitarbeiterRepository.findByEmailIgnoreCase(user.getUsername()))
				.thenReturn(Optional.of(mitarbeiter));

		UserDetails result = loginDetailsService.loadUserByUsername(email);

		assertEquals(user.getUsername(), result.getUsername());

	}

	@Test
	void UpperCaseBeiLoginIstIrrelevant() {
		String randomCaseEmail = "aLiCe.KRügER@eXampLe.cOm";
		LoginDetails user = new LoginDetails(mitarbeiter);

		Mockito.when(mitarbeiterRepository.findByEmailIgnoreCase(randomCaseEmail)).thenReturn(Optional.of(mitarbeiter));

		UserDetails result = loginDetailsService.loadUserByUsername(randomCaseEmail);

		assertEquals(user.getUsername(), result.getUsername());

	}

	@Test
	void loginFehlgeschlagen() {
		Mockito.when(mitarbeiterRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.empty());

		assertThrows(UsernameNotFoundException.class, () -> {
			loginDetailsService.loadUserByUsername(email);
		});

	}

}
