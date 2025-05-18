package com.npj.urlaubsplanung.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.npj.urlaubsplanung.dto.UserDto;

@Service
public class UserService {

	public UserService() {
	}

	public Iterable<UserDto> getUser() {
		return List.of(new UserDto("Alice", "Krüger", 24, 6), new UserDto("Bob", "Baumeister", 20, 10),
				new UserDto("Charlie", "Brown", 12, 18));
	}

	public UserDto getUserById() {
		return new UserDto("Alice", "Krüger", 24, 6);
	}
}
