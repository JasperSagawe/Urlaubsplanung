package com.npj.urlaubsplanung;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.authorizeHttpRequests(
						requests -> requests.requestMatchers("/login").permitAll().anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/login").permitAll()).build();
	}

	@Bean
	public UserDetailsService users() {
		UserDetails user = User.builder().username("user").password("{noop}password").roles("USER").build();
		UserDetails admin = User.builder().username("admin").password("{noop}password").roles("USER", "ADMIN").build();
		return new InMemoryUserDetailsManager(user, admin);
	}
}
