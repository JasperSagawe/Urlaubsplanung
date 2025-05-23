package com.npj.urlaubsplanung;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.npj.urlaubsplanung.dto.UserDto;
import com.npj.urlaubsplanung.model.Mitarbeiter;
import com.npj.urlaubsplanung.repository.MitarbeiterRepository;
import com.npj.urlaubsplanung.service.UserService;

@ExtendWith(MockitoExtension.class)
class getMitarbetier {
	
    @Mock
    MitarbeiterRepository mitarbeiterRepository;
	
	@InjectMocks
	UserService userService;

	@Test
	void test() {
		UserDto user = new UserDto("Alice@mail.de", "Krüger", 24, 6);
		Optional<Mitarbeiter> mitarbeiter = Optional.ofNullable(new Mitarbeiter("Alice@mail.de", "Krüger", "Alice@mail.de", null));
		
        Mockito.when(mitarbeiterRepository.findByEmail(user.getUsername())).thenReturn(mitarbeiter);
		
		UserDto result = userService.getUserByEMail("Alice@mail.de");
		
		assertEquals(user.getUsername(), result.getUsername());

	}

}
