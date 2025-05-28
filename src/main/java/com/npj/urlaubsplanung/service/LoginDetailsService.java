package com.npj.urlaubsplanung.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.npj.urlaubsplanung.repository.MitarbeiterRepository;
import com.npj.urlaubsplanung.security.LoginDetails;

@Service
public class LoginDetailsService implements UserDetailsService {

	private final MitarbeiterRepository mitarbeiterRepository;

	public LoginDetailsService(MitarbeiterRepository mitarbeiterRepository) {
		this.mitarbeiterRepository = mitarbeiterRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return mitarbeiterRepository.findByEmailIgnoreCase(email).map(LoginDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
	}
}
